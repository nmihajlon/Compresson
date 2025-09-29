# Analiza i kompresija binarnih fajlova

Ovaj projekat implementira algoritme za analizu i kompresiju binarnih fajlova. Demonstrira različite tehnike kompresije i izračunava entropiju bajtova binarnih podataka.

## Funkcionalnosti

1. **Izračunavanje bajt-entropije**
   - Računa učestalost svakog bajta (0–255) u binarnom fajlu.
   - Izračunava verovatnoću svakog bajta:  
     pi = Ni / N, gde je Ni broj pojavljivanja bajta i, a N ukupna dužina fajla u bajtovima.
   - Računa bajt-entropiju:  
     H(p) = - p0*log2(p0) - p1*log2(p1) - ... - p255*log2(p255)  
     (uz pretpostavku da je 0*log2(0) = 0).

2. **Shannon-Fano i Huffman kodiranje**
   - Konstrukcija Shannon-Fano i Huffman kodova na osnovu verovatnoća bajtova.
   - Kodiranje binarnog fajla korišćenjem generisanih kodova.
   - Čuvanje koda i kodiranih podataka u izlaznom fajlu.

3. **LZ77 i LZW kompresija**
   - Implementacija LZ77 i LZW algoritama za kompresiju sa rečnikom.
   - Kompresija binarnih fajlova sa skupom simbola A = {0, 1, ..., 255}.

4. **Efikasno skladištenje fajlova**
   - Struktura kodiranih fajlova dizajnirana da postigne optimalnu veličinu.
   - Efikasno rukovanje kodovima na nivou bitova pomoću 8-bitnog skladištenja.

## Pokretanje Java projekta
   - Kompajliranje: javac -d out src/org/example/models/GenerateSamples.java
   - Pokretanje: java -cp out org.example.models.GenerateSamples
