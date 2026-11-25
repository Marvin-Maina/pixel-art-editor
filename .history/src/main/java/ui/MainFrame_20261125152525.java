package ui;

import model.PixelGrid;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final JPanel swatch = new JPanel();

    public MainFrame() {
        super("Pixel Art Editor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        PixelGrid grid = new PixelGrid(32);
        CanvasPanel canvas = new CanvasPanel(grid, 16);

        // Colour swatch
        swatch.setPreferredSize(new Dimension(28, 28));
        swatch.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        swatch.setBackground(canvas.getCurrentColor());
        canvas.setOnColorPicked(swatch::setBackground);

        JButton colorBtn = new JButton("Pick colour");
        colorBtn.addActionListener(e -> {
            Color chosen = JColorChooser.showDialog(this, "Choose a colour", canvas.getCurrentColor());
            if (chosen != null) {
                canvas.setCurrentColor(chosen);
                swatch.setBackground(chosen);
            }
        });

        // Tool buttons: only one can be selected at a time
        ButtonGroup group = new ButtonGroup();
        JPanel tools = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tools.add(swatch);
        tools.add(colorBtn);
        tools.add(makeToolButton("Pencil", Tool.PENCIL, canvas, group, true));
        tools.add(makeToolButton("Eraser", Tool.ERASER, canvas, group, false));
        tools.add(makeToolButton("Fill", Tool.FILL, canvas, group, false));
        tools.add(makeToolButton("Picker", Tool.PICKER, canvas, group, false));

        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> {
            grid.clear();
            canvas.repaint();
        });
        tools.add(clearBtn);

        add(tools, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private JToggleButton makeToolButton(String label, Tool tool, CanvasPanel canvas,
                                         ButtonGroup group, boolean selected) {
        JToggleButton b = new JToggleButton(label, selected);
        b.addActionListener(e -> canvas.setTool(tool));
        group.add(b);
        return b;
    }
}