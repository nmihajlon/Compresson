package org.example.models;

public class FrequencyTable {
    private final long[] counts = new long[256];

    public void increment(int symbol){
        counts[symbol] += 1;
    }

    public long getCount(int symbol){
        return counts[symbol];
    }

    public long getTotal(){
        long sum = 0;
        for(long c : counts){
            sum += c;
        }
        return sum;
    }

    public long[] getCounts() {
        return counts;
    }
}
