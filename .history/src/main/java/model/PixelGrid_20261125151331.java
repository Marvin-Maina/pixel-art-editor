package model;

import java.util.Arra
import java.util.Arrays;

public class PixelGrid {
    public final int size;
    private final int[][] pixels;

    public PixelGrid(int size) {
        this.size = size;
        this.pixels = new int[size][size];
        clear();
    }

    public void clear() {
        for (int[] row : pixels) Arrays.fill(row, 0xFFFFFFFF); // opaque white
    }

    public int get(int x, int y) {
        return pixels[y][x];
    }

    public void set(int x, int y, int argb) {
        if (inBounds(x, y)) pixels[y][x] = argb;
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < size && y < size;
    }
}