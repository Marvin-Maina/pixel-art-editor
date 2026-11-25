package ui;

import model.PixelGrid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CanvasPanel extends JPanel {
    private final PixelGrid grid;
    private final int cell;
    private Color currentColor = Color.BLACK;

    public CanvasPanel(PixelGrid grid, int cell) {
        this.grid = grid;
        this.cell = cell;
        setPreferredSize(new Dimension(grid.size * cell, grid.size * cell));

        MouseAdapter painter = new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) { paintAt(e); }
            @Override public void mouseDragged(MouseEvent e) { paintAt(e); }
        };
        addMouseListener(painter);
        addMouseMotionListener(painter); // drag events come from this one
    }

    public void setCurrentColor(Color c) {
        this.currentColor = c;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    private void paintAt(MouseEvent e) {
        int x = e.getX() / cell;
        int y = e.getY() / cell;
        grid.set(x, y, currentColor.getRGB());
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

        g.setColor(Color.LIGHT_GRAY); // grid lines
        int total = grid.size * cell;
        for (int i = 0; i <= grid.size; i++) {
            g.drawLine(i * cell, 0, i * cell, total);
            g.drawLine(0, i * cell, total, i * cell);
        }
    }
}