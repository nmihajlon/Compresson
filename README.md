# Analiza i kompresija binarnih fajlova

Ovaj projekat implementira algoritme za analizu i kompresiju binarnih fajlova. Demonstrira različite tehnike kompresije i izračunava entropiju bajtova binarnih podataka.

## Funkcionalnosti

1. **Izračunavanje bajt-entropije**
   - Računa učestalost svakog bajta (0–255) u binarnom fajlu.
   - Izračunava verovatnoću svakog bajta:  
     \( p_i = \frac{N_i}{N} \), gde je \( N_i \) broj pojavljivanja bajta \( i \), a \( N \) ukupna dužina fajla u bajtovima.
   - Računa bajt-entropiju:  
     \[
     H(p) = -\sum_{i=0}^{255} p_i \log_2 p_i
     \]  
     (uz pretpostavku da je \(0 \log_2 0 = 0\)).

2. **Shannon-Fano i Huffman kodiranje**
   - Konstrukcija Shannon-Fano i Huffman kodova na osnovu verovatnoća bajtova.
   - Kodiranje binarnog fajla korišćenjem generisanih kodova.
   - Čuvanje koda i kodiranih podataka u izlaznom fajlu.

3. **LZ77 i LZW kompresija**
   - Implementacija LZ77 i LZW algoritama za kompresiju sa rečnikom.
   - Kompresija binarnih fajlova sa skupom simbola \( A = \{0, 1, \dots, 255\} \).

4. **Efikasno skladištenje fajlova**
   - Struktura kodiranih fajlova dizajnirana da postigne optimalnu veličinu.
   - Efikasno rukovanje kodovima na nivou bitova pomoću 8-bitnog skladištenja.

## Pokretanje Java projekta

1. **Preuzimanje projekta**
   ```bash
   git clone https://github.com/yourusername/binary-compression.git
   cd binary-compression


# Binary File Compression and Entropy Analysis

This project implements algorithms for analyzing and compressing binary files. It demonstrates different compression techniques and calculates the entropy of binary data.

## Features

1. **Byte-level Entropy Calculation**
   - Computes the frequency of each byte (0–255) in a binary file.
   - Calculates the probability of each byte:  
     \( p_i = \frac{N_i}{N} \), where \( N_i \) is the count of byte \( i \) and \( N \) is the total number of bytes.
   - Computes byte-level entropy:  
     \[
     H(p) = -\sum_{i=0}^{255} p_i \log_2 p_i
     \]  
     (with the convention \(0 \log_2 0 = 0\)).

2. **Shannon-Fano and Huffman Coding**
   - Constructs Shannon-Fano and Huffman codes based on byte probabilities.
   - Encodes the binary file using the generated codes.
   - Stores both the code and the encoded data in the output file.

3. **LZ77 and LZW Compression**
   - Implements LZ77 and LZW algorithms for dictionary-based compression.
   - Compresses binary files assuming an input alphabet \( A = \{0, 1, \dots, 255\} \).

4. **Efficient File Storage**
   - Designs encoded file structures to achieve asymptotically optimal size.
   - Handles bit-level codes efficiently using 8-bit storage.

## Usage

- Clone the repository:
  ```bash
  git clone https://github.com/yourusername/binary-compression.git
