package org.example;

import org.example.models.FrequencyTable;
import org.example.models.Node;

import java.io.*;
import java.util.*;

import static org.example.util.EntropyCalculator.calculateFrequency;

public class Huffman {

    // Kompresija fajla
    public static void compress(String inputFile, String outputFile) throws IOException {
        FrequencyTable frequencyTable = calculateFrequency(new File(inputFile));
        Node root = buildTree(frequencyTable);

        // mapiranje bajt -> kod
        Map<Integer, String> codes = new HashMap<>();
        generateCodes(root, "", codes);

        try (
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFile));
                FileInputStream fis = new FileInputStream(inputFile)
        ) {
            // upiši tabelu kodova
            dos.writeInt(codes.size());
            for (Map.Entry<Integer, String> entry : codes.entrySet()) {
                dos.writeByte(entry.getKey());
                dos.writeUTF(entry.getValue());
            }

            // kodiraj ulazne podatke u string bitova
            StringBuilder allBits = new StringBuilder();
            int byteValue;
            while ((byteValue = fis.read()) != -1) {
                allBits.append(codes.get(byteValue & 0xFF));
            }

            // upiši dužinu i same bitove kao bajtove
            dos.writeInt(allBits.length());
            for (int i = 0; i < allBits.length(); i += 8) {
                int b = 0;
                for (int j = 0; j < 8 && i + j < allBits.length(); j++) {
                    if (allBits.charAt(i + j) == '1') {
                        b |= (1 << (7 - j));
                    }
                }
                dos.writeByte(b);
            }
        }
    }

    // Dekompresija fajla
    public static void decompress(String inputFile, String outputFile) throws IOException {
        try (
                DataInputStream dis = new DataInputStream(new FileInputStream(inputFile));
                FileOutputStream fos = new FileOutputStream(outputFile)
        ) {
            int tableSize = dis.readInt();
            Map<String, Integer> codeToSymbol = new HashMap<>(tableSize);

            // učitaj kodnu tabelu
            for (int i = 0; i < tableSize; i++) {
                int symbol = dis.readUnsignedByte();
                String code = dis.readUTF();
                codeToSymbol.put(code, symbol);
            }

            int totalBits = dis.readInt();
            StringBuilder current = new StringBuilder();

            int bitsRead = 0;
            while (bitsRead < totalBits) {
                int oneByte = dis.readUnsignedByte();
                for (int j = 0; j < 8 && bitsRead < totalBits; j++) {
                    int bit = (oneByte >> (7 - j)) & 1;
                    current.append(bit == 1 ? '1' : '0');

                    Integer sym = codeToSymbol.get(current.toString());
                    if (sym != null) {
                        fos.write(sym);
                        current.setLength(0);
                    }
                    bitsRead++;
                }
            }
        }
    }

    // Gradi Huffman stablo
    private static Node buildTree(FrequencyTable freqTable) {
        LinkedList<Node> nodes = new LinkedList<>();
        for (int i = 0; i < 256; i++) {
            long count = freqTable.getCount(i);
            if (count > 0) {
                nodes.add(new Node(i, count));
            }
        }

        if (nodes.isEmpty()) return null;

        // Spajamo dok ne ostane samo jedan čvor
        while (nodes.size() > 1) {
            nodes.sort(Comparator.comparingLong(Node::getFrequency));
            Node left = nodes.removeFirst();
            Node right = nodes.removeFirst();
            nodes.add(new Node(left, right));
        }

        return nodes.getFirst();
    }

    // Generiše kodove
    private static void generateCodes(Node node, String code, Map<Integer, String> codes) {
        if (node.isLeaf()) {
            codes.put(node.getValue(), code.isEmpty() ? "0" : code);
        } else {
            generateCodes(node.getLeft(), code + "0", codes);
            generateCodes(node.getRight(), code + "1", codes);
        }
    }
}
