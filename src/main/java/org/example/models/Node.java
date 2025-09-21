package org.example.models;

public class Node implements Comparable<Node>{
    private final int value;
    private final long frequency;
    private final Node left;
    private final Node right;

    public Node(int value, long frequency){
        this.value = value;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }

    public Node(Node left, Node right){
        this.value = -1;
        this.frequency = left.frequency + right.frequency;
        this.left = left;
        this.right = right;
    }

    public boolean isLeaf(){
        return left == null && right == null;
    }

    public int getValue() {
        return value;
    }

    public long getFrequency() {
        return frequency;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    @Override
    public int compareTo(Node other){
        return Long.compare(this.frequency, other.frequency);
    }
}
