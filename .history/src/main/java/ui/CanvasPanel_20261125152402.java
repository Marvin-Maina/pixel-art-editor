package ui;

import model.PixelGrid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class CanvasPanel extends JPanel {
    private final PixelGrid grid;
    private final int cell;
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

    /** Called when the picker tool grabs a colour, so the UI can update its swatch. */
    public void setOnColorPicked(Consumer<Color> callback) { this.onColorPicked = callback; }

    private void apply(MouseEvent e, boolean isPress) {
        int x = e.getX() / cell;
        int y = e.getY() / cell;
        if (!grid.inBounds(x, y)) return;

        switch (tool) {
            case PENCIL -> grid.set(x, y, currentColor.getRGB());
            case ERASER -> grid.set(x, y, PixelGrid.BLANK);
            case FILL -> {
                if (isPress) grid.floodFill(x, y, currentColor.getRGB());
            }
            case PICKER -> {
                if (isPress) {
                    Color picked = new Color(grid.get(x, y), true);
                    currentColor = picked;
                    onColorPicked.accept(picked);
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
                g.setColor(new Color(grid.get(x, y), true));
                g.fillRect(x * cell, y * cell, cell, cell);
            }
        }

        g.setColor(Color.LIGHT_GRAY);
        int total = grid.size * cell;
        for (int i = 0; i <= grid.size; i++) {
            g.drawLine(i * cell, 0, i * cell, total);
            g.drawLine(0, i * cell, total, i * cell);
        }
    }
}