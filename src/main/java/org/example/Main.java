package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import static org.example.util.EntropyCalculator.calculateEntropy;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Upotreba: java EntropyCalculator <putanja_do_fajla>");
            return;
        }

        File file = new File(args[0]);
        File input = new File(args[0]);
        File compressed = new File(input.getAbsolutePath() + ".huff");
        File decompressed = new File(input.getAbsolutePath() + ".dec");

        try{
            double entropy = calculateEntropy(file);
            System.out.printf("Bajt-entropija fajla '%s' = %.6f bitova po bajtu%n", file.getName(), entropy);

            System.out.println("Pokrecem Huffman kompresiju...");
            Huffman.compress(input.getAbsolutePath(), compressed.getAbsolutePath());
            System.out.printf("Original: %d B, Kompresovan: %d B%n",
                    input.length(), compressed.length());

            System.out.println("Pokrecem Huffman dekompresiju...");
            Huffman.decompress(compressed.getAbsolutePath(), decompressed.getAbsolutePath());

            boolean isti = Arrays.equals(
                    Files.readAllBytes(input.toPath()),
                    Files.readAllBytes(decompressed.toPath())
            );

            System.out.println("Provera dekompresije: " + (isti ? "USPESNA" : "NEUSPESNA"));

        } catch (IOException e){
            System.err.println("Greska prilikom citanja fajla: " + e.getMessage());
        }
    }
}