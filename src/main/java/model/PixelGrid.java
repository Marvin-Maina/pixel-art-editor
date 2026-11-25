package model;

import java.util.ArrayDeque;
import java.util.Deque;

public class PixelGrid {
    public static final int BLANK = 0x00000000; // fully transparent

    public final int size;
    private final int[][] pixels;

    public PixelGrid(int size) {
        this.size = size;
        this.pixels = new int[size][size];
        clear();
    }

    public void clear() {
        for (int[] row : pixels) java.util.Arrays.fill(row, BLANK);
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

    /** Returns an independent copy of all pixels. */
    public int[][] snapshot() {
        int[][] copy = new int[size][];
        for (int y = 0; y < size; y++) copy[y] = pixels[y].clone();
        return copy;
    }

    /** Overwrites all pixels with a previously taken snapshot. */
    public void restore(int[][] data) {
        for (int y = 0; y < size; y++) {
            System.arraycopy(data[y], 0, pixels[y], 0, size);
        }
    }

    /** Replaces the connected region of same-coloured cells around (sx, sy). */
    public void floodFill(int sx, int sy, int newColor) {
        if (!inBounds(sx, sy)) return;
        int target = get(sx, sy);
        if (target == newColor) return;

        Deque<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{sx, sy});

        while (!queue.isEmpty()) {
            int[] p = queue.poll();
            int x = p[0], y = p[1];
            if (!inBounds(x, y) || get(x, y) != target) continue;

            set(x, y, newColor);
            queue.add(new int[]{x + 1, y});
            queue.add(new int[]{x - 1, y});
            queue.add(new int[]{x, y + 1});
            queue.add(new int[]{x, y - 1});
        }
    }
}