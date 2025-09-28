package org.example;

import org.example.models.FrequencyTable;
import org.example.models.Symbol;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.util.EntropyCalculator.calculateFrequency;

public class ShannonFano {

    public static void compress(String inputFile, String outputFile) throws IOException {
        FrequencyTable frequencyTable = calculateFrequency(new File(inputFile));

        // Lista simbola sa frekvencijama
        List<Symbol> symbols = new ArrayList<>();
        for(int i = 0; i < 256; i++){
            long count = frequencyTable.getCount(i);
            if(count > 0){
                symbols.add(new Symbol(i, count));
            }
        }

        symbols.sort((a, b) -> Long.compare(b.getFrequency(), a.getFrequency()));

        Map<Integer, String> codes = new HashMap<>();
        buildCodes(symbols, codes, "");

        try (
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFile));
            FileInputStream fis = new FileInputStream(inputFile)
        ) {
            dos.writeInt(codes.size());
            for (Map.Entry<Integer, String> entry : codes.entrySet()) {
                dos.writeByte(entry.getKey());
                dos.writeUTF(entry.getValue());
            }

            StringBuilder allBits = new StringBuilder();
            int b;
            while ((b = fis.read()) != -1) {
                allBits.append(codes.get(b & 0xFF));
            }

            dos.writeInt(allBits.length());
            for (int i = 0; i < allBits.length(); i += 8) {
                int outByte = 0;
                for (int j = 0; j < 8 && i + j < allBits.length(); j++) {
                    if (allBits.charAt(i + j) == '1') {
                        outByte |= (1 << (7 - j));
                    }
                }
                dos.writeByte(outByte);
            }
        }
    }

    public static void decompress(String inputFile, String outputFile) throws IOException {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(inputFile));
             FileOutputStream fos = new FileOutputStream(outputFile)) {

            int tableSize = dis.readInt();
            if (tableSize == 0) {
                // nema podataka
                return;
            }

            Map<String, Integer> codeToSymbol = new HashMap<>(tableSize);

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

    private static void buildCodes(List<Symbol> symbols, Map<Integer, String> codes, String prefix) {
        if (symbols.size() == 1) {
            codes.put(symbols.get(0).getValue(), prefix.isEmpty() ? "0" : prefix);
            return;
        }

        int splitIndex = findSplit(symbols);
        List<Symbol> left = symbols.subList(0, splitIndex);
        List<Symbol> right = symbols.subList(splitIndex, symbols.size());

        buildCodes(new ArrayList<>(left), codes, prefix + "0");
        buildCodes(new ArrayList<>(right), codes, prefix + "1");
    }

    private static int findSplit(List<Symbol> symbols) {
        long total = 0;
        for (Symbol s : symbols) total += s.getFrequency();
        long half = total / 2;
        long sum = 0;
        for (int i = 0; i < symbols.size(); i++) {
            sum += symbols.get(i).getFrequency();
            if (sum >= half) return i + 1;
        }
        return symbols.size();
    }
}
