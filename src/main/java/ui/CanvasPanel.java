package ui;

import model.History;
import model.PixelGrid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class CanvasPanel extends JPanel {
    private static final Color CHECK_LIGHT = new Color(0xFFFFFF);
    private static final Color CHECK_DARK = new Color(0xCCCCCC);
    private static final Color GRID_LINE = new Color(0, 0, 0, 50);

    private final PixelGrid grid;
    private final int cell;
    private final History history = new History();

    private Color currentColor = Color.BLACK;
    private Tool tool = Tool.PENCIL;
    private Consumer<Color> onColorPicked = c -> {};

    public CanvasPanel(PixelGrid grid, int cell) {
        this.grid = grid;
        this.cell = cell;
        setPreferredSize(new Dimension(grid.size * cell, grid.size * cell));

        MouseAdapter mouse = new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) { apply(e, true); }
            @Override public void mouseDragged(MouseEvent e) { apply(e, false); }
        };
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    public void setTool(Tool tool) { this.tool = tool; }

    public void setCurrentColor(Color c) { this.currentColor = c; }

    public Color getCurrentColor() { return currentColor; }

    public void setOnColorPicked(Consumer<Color> callback) { this.onColorPicked = callback; }

    public void undo() {
        int[][] previous = history.undo(grid.snapshot());
        if (previous != null) {
            grid.restore(previous);
            repaint();
        }
    }

    public void redo() {
        int[][] next = history.redo(grid.snapshot());
        if (next != null) {
            grid.restore(next);
            repaint();
        }
    }

    /** Clearing is undoable too, so it records first. */
    public void clearCanvas() {
        history.record(grid.snapshot());
        grid.clear();
        repaint();
    }

    private void apply(MouseEvent e, boolean isPress) {
        // floorDiv rounds toward negative infinity, so dragging off the left or
        // top edge gives -1 (out of bounds) instead of 0 (a wrong cell)
        int x = Math.floorDiv(e.getX(), cell);
        int y = Math.floorDiv(e.getY(), cell);
        if (!grid.inBounds(x, y)) return;

        // One snapshot per stroke: on press only, never on drag
        if (isPress && tool != Tool.PICKER) {
            history.record(grid.snapshot());
        }

        switch (tool) {
            case PENCIL -> grid.set(x, y, currentColor.getRGB());
            case ERASER -> grid.set(x, y, PixelGrid.BLANK);
            case FILL -> {
                if (isPress) grid.floodFill(x, y, currentColor.getRGB());
            }
            case PICKER -> {
                if (isPress) {
                    int argb = grid.get(x, y);
                    if ((argb >>> 24) != 0) { // ignore transparent cells
                        Color picked = new Color(argb, true);
                        currentColor = picked;
                        onColorPicked.accept(picked);
                    }
                }
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int y = 0; y < grid.size; y++) {
            for (int x = 0; x < grid.size; x++) {
                // checkerboard first, so transparent cells show through to it
                g.setColor((x + y) % 2 == 0 ? CHECK_LIGHT : CHECK_DARK);
                g.fillRect(x * cell, y * cell, cell, cell);

                int argb = grid.get(x, y);
                if ((argb >>> 24) != 0) { // alpha > 0 means something is painted
                    g.setColor(new Color(argb, true));
                    g.fillRect(x * cell, y * cell, cell, cell);
                }
            }
        }

        g.setColor(GRID_LINE);
        int total = grid.size * cell;
        for (int i = 0; i <= grid.size; i++) {
            g.drawLine(i * cell, 0, i * cell, total);
            g.drawLine(0, i * cell, total, i * cell);
        }
    }
}