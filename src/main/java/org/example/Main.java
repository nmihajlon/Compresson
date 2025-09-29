package org.example;

import org.example.util.EntropyCalculator;
import org.example.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Main {
    private static PrintWriter report;

    public static void main(String[] args) {
        try {
            org.example.models.GenerateSamples.main(new String[]{});
        } catch (IOException e) {
            System.err.println("Greska pri generisanju fajlova: " + e.getMessage());
        }

        String in1 = (args.length >= 1) ? args[0] : "sample.bin";
        String in2 = (args.length >= 2) ? args[1] : "sample.txt";

        String reportFile = "izvestaj.txt";

        // Huffman fajlovi
        String huffC1 = in1 + ".huff";
        String huffD1 = in1 + ".huff.dec";
        String huffC2 = in2 + ".huff";
        String huffD2 = in2 + ".huff.dec";

        // Shannon–Fano fajlovi
        String sfC1 = in1 + ".sf";
        String sfD1 = in1 + ".sf.dec";
        String sfC2 = in2 + ".sf";
        String sfD2 = in2 + ".sf.dec";

        // LZ77 fajlovi
        String lz77C1 = in1 + ".lz77";
        String lz77D1 = in1 + ".lz77.dec";
        String lz77C2 = in2 + ".lz77";
        String lz77D2 = in2 + ".lz77.dec";

        // LZW fajlovi
        String lzwC1 = in1 + ".lzw";
        String lzwD1 = in1 + ".lzw.dec";
        String lzwC2 = in2 + ".lzw";
        String lzwD2 = in2 + ".lzw.dec";

        try {
            report = new PrintWriter(reportFile);

            double ent1 = EntropyCalculator.calculateEntropy(Path.of(in1).toFile());
            double ent2 = EntropyCalculator.calculateEntropy(Path.of(in2).toFile());

            logf("Entropija %s: %.4f%n", in1, ent1);
            logf("Entropija %s: %.4f%n%n", in2, ent2);

            log("HUFFMAN");
            processHuffman(in1, huffC1, huffD1);
            processHuffman(in2, huffC2, huffD2);

            log("\nSHANNON–FANO");
            processShannonFano(in1, sfC1, sfD1);
            processShannonFano(in2, sfC2, sfD2);

            log("\nLZ77");
            processLZ77(in1, lz77C1, lz77D1);
            processLZ77(in2, lz77C2, lz77D2);

            log("\nLZW");
            processLZW(in1, lzwC1, lzwD1);
            processLZW(in2, lzwC2, lzwD2);

            log("\nIzvestaj sacuvan u: " + reportFile);
            report.close();
        } catch (IOException e) {
            System.err.println("Greska: " + e.getMessage());
        }
    }

    private static void processHuffman(String input, String compressed, String decompressed) throws IOException {
        long inSize = Files.size(Path.of(input));

        long t1 = System.nanoTime();
        Huffman.compress(input, compressed);
        long t2 = System.nanoTime();

        long cSize = Files.size(Path.of(compressed));

        long t3 = System.nanoTime();
        Huffman.decompress(compressed, decompressed);
        long t4 = System.nanoTime();

        printStats("Huffman", input, decompressed, inSize, cSize, t2 - t1, t4 - t3);
    }

    private static void processShannonFano(String input, String compressed, String decompressed) throws IOException {
        long inSize = Files.size(Path.of(input));

        long t1 = System.nanoTime();
        ShannonFano.compress(input, compressed);
        long t2 = System.nanoTime();

        long cSize = Files.size(Path.of(compressed));

        long t3 = System.nanoTime();
        ShannonFano.decompress(compressed, decompressed);
        long t4 = System.nanoTime();

        printStats("Shannon-Fano", input, decompressed, inSize, cSize, t2 - t1, t4 - t3);
    }

    private static void processLZ77(String input, String compressed, String decompressed) throws IOException {
        long inSize = Files.size(Path.of(input));

        long t1 = System.nanoTime();
        LZ77.compress(input, compressed);
        long t2 = System.nanoTime();

        long cSize = Files.size(Path.of(compressed));

        long t3 = System.nanoTime();
        LZ77.decompress(compressed, decompressed);
        long t4 = System.nanoTime();

        printStats("LZ77", input, decompressed, inSize, cSize, t2 - t1, t4 - t3);
    }

    private static void processLZW(String input, String compressed, String decompressed) throws IOException {
        long inSize = Files.size(Path.of(input));

        long t1 = System.nanoTime();
        LZW.compress(input, compressed);
        long t2 = System.nanoTime();

        long cSize = Files.size(Path.of(compressed));

        long t3 = System.nanoTime();
        LZW.decompress(compressed, decompressed);
        long t4 = System.nanoTime();

        printStats("LZW", input, decompressed, inSize, cSize, t2 - t1, t4 - t3);
    }

    private static void printStats(String method, String input, String decompressed,
                                   long inSize, long cSize, long compressTime, long decompressTime) throws IOException {
        double ratio = (double) cSize / inSize;
        boolean equal = filesEqual(input, decompressed);

        logf("%s (%s)%n", method, input);
        logf("  Ulaz: %d B, Kompresovano: %d B, Odnos: %.4f%n", inSize, cSize, ratio);
        logf("  Vreme kompresije: %.2f ms, dekompresije: %.2f ms%n",
                compressTime / 1e6, decompressTime / 1e6);
        logf("  Provera fajlova: %s%n%n", equal ? "ISTO" : "RAZLICITO");
    }

    private static void log(String s) {
        System.out.println(s);
        report.println(s);
    }

    private static void logf(String format, Object... args) {
        System.out.printf(format, args);
        report.printf(format, args);
    }

    private static boolean filesEqual(String f1, String f2) throws IOException {
        return Arrays.equals(Files.readAllBytes(Path.of(f1)), Files.readAllBytes(Path.of(f2)));
    }
}