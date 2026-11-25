package model;

import java.util.ArrayDeque;
import java.util.Deque;

public class History {
    private final Deque<int[][]> undoStack = new ArrayDeque<>();
    private final Deque<int[][]> redoStack = new ArrayDeque<>();

    public void record(int[][] snapshot) {
        undoStack.push(snapshot);
        redoStack.clear();
    }

    public int[][] undo(int[][] current) {
        if (undoStack.isEmpty()) return null;

        redoStack.push(clone(current));
        return undoStack.pop();
    }

    public int[][] redo(int[][] current) {
        if (redoStack.isEmpty()) return null;

        undoStack.push(clone(current));
        return redoStack.pop();
    }

    private int[][] clone(int[][] snapshot) {
        int[][] copy = new int[snapshot.length][];
        for (int y = 0; y < snapshot.length; y++) {
            copy[y] = snapshot[y].clone();
        }
        return copy;
    }
}
