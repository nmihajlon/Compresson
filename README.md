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
