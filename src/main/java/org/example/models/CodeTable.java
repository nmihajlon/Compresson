package org.example.models;

import java.util.HashMap;
import java.util.Map;

public class CodeTable {
    private final Map<Integer, String> codes = new HashMap<>();

    public void put(int symbol, String bits) {
        codes.put(symbol, bits);
    }

    public String get(int symbol) {
        return codes.get(symbol);
    }

    public Map<Integer, String> getAll(){
        return codes;
    }
}
