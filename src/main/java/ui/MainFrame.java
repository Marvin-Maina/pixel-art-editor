package ui;

import model.PixelGrid;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        super("Pixel Art Editor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        PixelGrid grid = new PixelGrid(32);
        CanvasPanel canvas = new CanvasPanel(grid, 16);

        JButton colorBtn = new JButton("Pick colour");
        colorBtn.addActionListener(e -> {
            Color chosen = JColorChooser.showDialog(this, "Choose a colour", canvas.getCurrentColor());
            if (chosen != null) canvas.setCurrentColor(chosen);
        });

        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> {
            grid.clear();
            canvas.repaint();
        });

        JPanel tools = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tools.add(colorBtn);
        tools.add(clearBtn);

        add(tools, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);

        pack();                       // size the window to fit the canvas
        setResizable(false);
        setLocationRelativeTo(null);
    }
}