package org.example.util;

import org.example.models.FrequencyTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class EntropyCalculator {
    public static FrequencyTable calculateFrequency(File file) throws IOException {
        FrequencyTable frequencyTable = new FrequencyTable();
        try(FileInputStream fis = new FileInputStream(file)){
            byte[] buffer = new byte[8192];
            int bytesRead;

            while((bytesRead = fis.read(buffer)) != -1){
                for(int i = 0; i < bytesRead; i++){
                    frequencyTable.increment(buffer[i] & 0xFF); // maska da bude 0-255
                }
            }
        }
        return frequencyTable;
    }

    public static double calculateEntropy(FrequencyTable frequencyTable){
        double entropy = 0.0;
        long total = frequencyTable.getTotal();
        if(total == 0)
            return 0.0;

        for(int i = 0; i < 256; i++){
            long count = frequencyTable.getCount(i);
            if(count == 0) continue;
            double p = (double) count / total;
            entropy += -p * (Math.log(p) / Math.log(2)); // log2(p)
        }
        return entropy;
    }

    public static double calculateEntropy(File file) throws IOException{
        FrequencyTable frequencyTable = calculateFrequency(file);
        return calculateEntropy(frequencyTable);
    }
}
