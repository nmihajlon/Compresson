package org.example.models;

public class Header {
    private final String algorithm;
    private final long originalSize;

    public Header(String algorithm, long originalSize){
        this.algorithm = algorithm;
        this.originalSize = originalSize;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public long getOriginalSize() {
        return originalSize;
    }
}
