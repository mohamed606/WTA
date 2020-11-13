package com.company;

public class Weapons {
    private final String name;
    private int indexInMatrix;

    public String getName() {
        return name;
    }

    public int getIndexInMatrix() {
        return indexInMatrix;
    }

    public void setIndexInMatrix(int indexInMatrix) {
        this.indexInMatrix = indexInMatrix;
    }

    public Weapons(String name) {
        this.name = name;
    }
}
