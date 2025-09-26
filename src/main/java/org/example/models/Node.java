package org.example.models;

public class Node implements Comparable<Node>{
    private int value;
    private long frequency;
    private Node left;
    private Node right;

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

    public void setValue(int value) {
        this.value = value;
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
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
