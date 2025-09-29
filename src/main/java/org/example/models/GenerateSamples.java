package org.example.models;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class GenerateSamples {
    public static void main(String[] args) throws IOException {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        int length = 15000;
        int lineLength = 100;
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
            if ((i + 1) % lineLength == 0) {
                sb.append("\n");
            }
        }
        Files.writeString(Path.of("sample.txt"), sb.toString());

        byte[] bytes = new byte[1024];
        new Random().nextBytes(bytes);
        try (FileOutputStream fos = new FileOutputStream("sample.bin")) {
            fos.write(bytes);
        }

        System.out.println("Fajlovi sample.txt i sample.bin su generisani.");
    }
}
