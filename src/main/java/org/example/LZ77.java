package org.example;

import org.example.models.Token;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LZ77 {
    private static final int WINDOW_SIZE = 255;
    private static final int LOOK_AHEAD_SIZE = 15;

    public static void compress(String inputFile, String outputFile) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile));
             DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)))) {

            byte[] input = bis.readAllBytes();
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

    public static void decompress(String inputFile, String outputFile) throws IOException {
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(inputFile)));
             FileOutputStream fos = new FileOutputStream(outputFile)) {

            int numTokens = dis.readInt();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            for (int i = 0; i < numTokens; i++) {
                int offset = dis.readUnsignedByte();
                int length = dis.readUnsignedByte();
                int symbol = dis.readUnsignedByte();

                if (offset > out.size()) {
                    throw new IOException("Invalid token: offset je veci od output buffer-a ");
                }

                // Kopiranje duplikata iz prethodnih bajtova
                for (int j = 0; j < length; j++) {
                    if (offset > 0) {
                        byte b = out.toByteArray()[out.size() - offset];
                        out.write(b);
                        fos.write(b);
                    }
                }

                // Dodavanje simbol-a
                out.write((byte) symbol);
                fos.write(symbol);
            }
        }
    }

    private static Token findLongestMatch(final byte[] input, final int pos) {
        int bestLength = 0;
        int bestOffset = 0;

        int searchStart = Math.max(0, pos - WINDOW_SIZE);
        int maxLength = Math.min(LOOK_AHEAD_SIZE, input.length - pos - 1);

        if (pos == input.length - 1) {
            return new Token(0, 0, input[pos] & 0xFF);
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
