package org.example;

import org.example.models.FrequencyTable;
import org.example.models.Node;

import java.io.*;
import java.util.*;

import static org.example.util.EntropyCalculator.calculateFrequency;

public class Huffman {

    // Kompresija fajla
    public static void compress(String inputFile, String outputFile) throws IOException {
        File inFile = new File(inputFile);
        FrequencyTable frequencyTable = calculateFrequency(new File(inputFile));
        Node root = buildTree(frequencyTable);

        // mapiranje bajt -> kod
        Map<Integer, String> codes = new HashMap<>();
        generateCodes(root, "", codes);

        try (
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFile));
                FileInputStream fis = new FileInputStream(inputFile)
        ) {
            // upisujem originalnu velicinu
            dos.writeLong(inFile.length());

            // upis tabele kodova
            dos.writeInt(codes.size());
            for (Map.Entry<Integer, String> entry : codes.entrySet()) {
                dos.writeByte(entry.getKey());
                String code = entry.getValue();
                dos.writeByte(code.length());

                int b = 0;
                int bitCount = 0;
                for(char c: code.toCharArray()){
                    b = (b << 1) | (c == '1' ? 1 : 0);
                    bitCount++;
                    if(bitCount == 8){
                        dos.writeByte(b);
                        b = 0;
                        bitCount = 0;
                    }
                }

                if(bitCount > 0){
                    b <<= (8 - bitCount);
                    dos.writeByte(b);
                }
            }

            // kodiranje ulaznih podataka
            int buffer = 0;
            int bitCount = 0;
            int byteValue;
            while ((byteValue = fis.read()) != -1) {
                String code = codes.get(byteValue & 0xFF);
                for (char c : code.toCharArray()) {
                    buffer = (buffer << 1) | (c == '1' ? 1 : 0);
                    bitCount++;
                    if (bitCount == 8) {
                        dos.writeByte(buffer);
                        buffer = 0;
                        bitCount = 0;
                    }
                }
            }
            if (bitCount > 0) {
                buffer <<= (8 - bitCount);
                dos.writeByte(buffer);
            }
        }
    }

    // Dekompresija fajla
    public static void decompress(String inputFile, String outputFile) throws IOException {
        try (
                DataInputStream dis = new DataInputStream(new FileInputStream(inputFile));
                FileOutputStream fos = new FileOutputStream(outputFile)
        ) {
            long originalSize = dis.readLong();
            int tableSize = dis.readInt();
            Map<Integer, String> codes = new HashMap<>(tableSize);

            // ucitaj kodnu tabelu
            for (int i = 0; i < tableSize; i++) {
                int symbol = dis.readUnsignedByte();
                int codeLength = dis.readByte() & 0xFF;
                StringBuilder sb = new StringBuilder();
                int bitsRead = 0;

                while(bitsRead < codeLength){
                    int oneByte = dis.readUnsignedByte();
                    for(int j = 7; j >= 0 && bitsRead < codeLength; j--){
                        int bit = (oneByte >> j) & 1;
                        sb.append(bit == 1 ? '1' : '0');
                        bitsRead++;
                    }
                }
                codes.put(symbol, sb.toString());
            }

            Node root = rebuildTree(codes);


            Node current = root;
            long bytesWritten = 0;
            while (bytesWritten < originalSize) {
                int oneByte = dis.readUnsignedByte();
                for (int j = 7; j >= 0 && bytesWritten < originalSize; j--) {
                    int bit = (oneByte >> j) & 1;
                    current = (bit == 0) ? current.getLeft() : current.getRight();
                    if (current.isLeaf()) {
                        fos.write(current.getValue());
                        current = root;
                        bytesWritten++;
                    }
                }
            }
        }
    }

    private static Node buildTree(FrequencyTable freqTable) {
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingLong(Node::getFrequency));

        for (int i = 0; i < 256; i++) {
            long count = freqTable.getCount(i);
            if (count > 0) {
                pq.add(new Node(i, count));
            }
        }

        if (pq.isEmpty()) return null;

        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            pq.add(new Node(left, right));
        }

        return pq.poll();
    }

    // Gradi Huffman stablo iz frekvencija
    private static Node rebuildTree(Map<Integer, String> codes) {
        Node root = new Node(-1, 0);
        for (Map.Entry<Integer, String> entry : codes.entrySet()) {
            int symbol = entry.getKey();
            String code = entry.getValue();
            root = insertNode(root, code, symbol);
        }
        return root;
    }

    private static Node insertNode(Node node, String code, int symbol) {
        if (code.isEmpty()) {
            node.setValue(symbol); // ubaci vrednost u leaf
            return node;
        }

        char c = code.charAt(0);
        if (c == '0') {
            if (node.getLeft() == null) node.setLeft(new Node(-1, 0));
            insertNode(node.getLeft(), code.substring(1), symbol);
        } else {
            if (node.getRight() == null) node.setRight(new Node(-1, 0));
            insertNode(node.getRight(), code.substring(1), symbol);
        }

        return node;
    }

    // Generise kodove
    private static void generateCodes(Node node, String code, Map<Integer, String> codes) {
        if (node.isLeaf()) {
            codes.put(node.getValue(), code.isEmpty() ? "0" : code);
        } else {
            generateCodes(node.getLeft(), code + "0", codes);
            generateCodes(node.getRight(), code + "1", codes);
        }
    }
}
