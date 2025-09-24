package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LZW {
    // 12 bitni kod
    private static final int MAX_DICT_SIZE = 4096;

    public static void compress(String inputFile, String outputFile) throws IOException {
        byte[] input = new FileInputStream(inputFile).readAllBytes();

        // recnik
        Map<String, Integer> dict = new HashMap<>();
        for(int i = 0; i < 256; i++){
            dict.put("" + (char) i, i);
        }
        int dictSize = 256;

        List<Integer> result = new ArrayList<>();

        String w = "";
        for(byte b: input){
            char c = (char) (b & 0xFF);
            String wc = w + c;
            if(dict.containsKey(wc)){
                w = wc;
            }
            else{
                result.add(dict.get(w));
                if(dictSize < MAX_DICT_SIZE){
                    dict.put(wc, dictSize++);
                }
                w = "" + c;
            }
        }

        if(!w.isEmpty()){
            result.add(dict.get(w));
        }

        try(DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFile))){
            dos.writeInt(result.size());
            for(int code : result){
                dos.writeShort(code);
            }
        }
    }

    public static void decompress(String inputFile, String outputFile) throws IOException {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(inputFile));
             FileOutputStream fos = new FileOutputStream(outputFile)) {

            int numCodes = dis.readInt();
            int[] codes = new int[numCodes];
            for (int i = 0; i < numCodes; i++) {
                codes[i] = dis.readUnsignedShort();
            }

            // inicijalni rečnik
            Map<Integer, String> dict = new HashMap<>();
            for (int i = 0; i < 256; i++) {
                dict.put(i, "" + (char) i);
            }
            int dictSize = 256;

            String w = "" + (char) codes[0];
            fos.write(w.getBytes());
            for (int i = 1; i < codes.length; i++) {
                int k = codes[i];
                String entry;
                if (dict.containsKey(k)) {
                    entry = dict.get(k);
                } else if (k == dictSize) {
                    entry = w + w.charAt(0);
                } else {
                    throw new IOException("Bad LZW code: " + k);
                }

                fos.write(entry.getBytes());

                if (dictSize < MAX_DICT_SIZE) {
                    dict.put(dictSize++, w + entry.charAt(0));
                }
                w = entry;
            }
        }
    }
}
