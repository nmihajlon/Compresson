package org.example;

import org.example.models.Token;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LZ77 {
    private static final int WINDOW_SIZE = 255;
    private static final int LOOK_AHEAD_SIZE = 15;

    public static void compress(String inputFile, String outputFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(inputFile);
             DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFile))) {

            byte[] input = fis.readAllBytes();
            List<Token> tokens = new ArrayList<>();

            int pos = 0;
            while (pos < input.length) {
                Token t = findLongestMatch(input, pos);
                tokens.add(t);

                pos += t.getLength() + 1;
            }

            dos.writeInt(tokens.size());

            for (Token t : tokens) {
                dos.writeByte(t.getOffset() & 0xFF);
                dos.writeByte(t.getLength() & 0xFF);
                dos.writeByte(t.getSymbol() & 0xFF);
            }
        }
    }

    // Dekompresija
    public static void decompress(String inputFile, String outputFile) throws IOException {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(inputFile));
             FileOutputStream fos = new FileOutputStream(outputFile)) {

            int numTokens = dis.readInt();
            List<Byte> out = new ArrayList<>();

            for (int i = 0; i < numTokens; i++) {
                int offset = dis.readUnsignedByte();
                int length = dis.readUnsignedByte();
                int symbol = dis.readUnsignedByte();

                for (int j = 0; j < length; j++) {
                    if (offset > 0 && out.size() >= offset) {
                        byte b = out.get(out.size() - offset);
                        out.add(b);
                        fos.write(b & 0xFF);
                    }
                }

                out.add((byte) (symbol & 0xFF));
                fos.write(symbol & 0xFF);
            }
        }
    }

    private static Token findLongestMatch(final byte[] input, final int pos) {
        int bestLength = 0;
        int bestOffset = 0;

        int searchStart = Math.max(0, pos - WINDOW_SIZE);
        int maxLength = Math.min(LOOK_AHEAD_SIZE, input.length - pos - 1);

        if (pos == input.length - 1) {
            int last = input[pos] & 0xFF;
            return new Token(0, 0, last);
        }

        for (int i = searchStart; i < pos; i++) {
            int length = 0;

            while (length < maxLength &&
                    (pos + length) < (input.length - 1) &&
                    input[i + length] == input[pos + length]) {
                length++;
            }

            if (length > bestLength) {
                bestLength = length;
                bestOffset = pos - i;
                if (bestLength == maxLength) break;
            }
        }

        int symbolIndex = pos + bestLength;
        int symbol = (symbolIndex < input.length) ? (input[symbolIndex] & 0xFF) : 0;

        if (bestOffset > 255) bestOffset = 255;
        if (bestLength > 255) bestLength = 255;

        return new Token(bestOffset, bestLength, symbol);
    }
}
