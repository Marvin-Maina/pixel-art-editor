package model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Arrays;

public class PixelGrid {
    public static final int BLANK = 0xFFFFFFFF; // opaque white
    public final int size;
    private final int[][] pixels;

    public PixelGrid(int size) {
        this.size = size;
        this.pixels = new int[size][size];
        clear();
    }

    public void clear() {
        for (int[] row : pixels) Arrays.fill(row, BLANK); // opaque white
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


    public void floodFill(int sx, int sy, int newColor) {
            if (!inBounds(sx, sy)) return;
            int target = get(sx, sy);
            if (target == newColor) return;

            Deque<int[]> queue = new ArrayDeque<>();
            queue.add(new int[]{sx, sy});

            while ()
    }