package org.example;

import java.io.File;
import java.io.IOException;

import static org.example.util.EntropyCalculator.calculateEntropy;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Upotreba: java EntropyCalculator <putanja_do_fajla>");
            return;
        }

        File file = new File(args[0]);
        try{
            double entropy = calculateEntropy(file);
            System.out.printf("Bajt-entropija fajla '%s' = %.6f bitova po bajtu%n", file.getName(), entropy);

        } catch (IOException e){
            System.err.println("Greska prilikom citanja fajla: " + e.getMessage());
        }
    }
}