package ui;

import io.ImageExporter;
import model.PixelGrid;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;

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

        // Tool buttons
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

        // Export controls
        JComboBox<Integer> scaleBox = new JComboBox<>(new Integer[]{1, 4, 8, 16, 32});
        scaleBox.setSelectedItem(16);
        JButton exportBtn = new JButton("Export PNG");
        exportBtn.addActionListener(e -> exportImage(grid, (Integer) scaleBox.getSelectedItem()));
        tools.add(new JLabel("Scale:"));
        tools.add(scaleBox);
        tools.add(exportBtn);

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

    private void exportImage(PixelGrid grid, int scale) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Export as PNG");
        chooser.setFileFilter(new FileNameExtensionFilter("PNG image (*.png)", "png"));
        chooser.setSelectedFile(new File("pixel-art.png"));

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".png")) {
            file = new File(file.getAbsolutePath() + ".png");   // add extension if missing
        }

        if (file.exists()) {
            int answer = JOptionPane.showConfirmDialog(this,
                    file.getName() + " already exists. Replace it?",
                    "Confirm overwrite", JOptionPane.YES_NO_OPTION);
            if (answer != JOptionPane.YES_OPTION) return;
        }

        try {
            ImageExporter.exportPng(grid, file, scale);
            JOptionPane.showMessageDialog(this,
                    "Saved " + (grid.size * scale) + "x" + (grid.size * scale) + " image to:\n" + file,
                    "Export complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not save the image:\n" + ex.getMessage(),
                    "Export failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}