package ui;

import io.ImageExporter;
import model.PixelGrid;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
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

        // Row 1: colour and tools
        ButtonGroup group = new ButtonGroup();
        JPanel toolRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolRow.add(swatch);
        toolRow.add(colorBtn);
        toolRow.add(makeToolButton("Pencil", Tool.PENCIL, canvas, group, true));
        toolRow.add(makeToolButton("Eraser", Tool.ERASER, canvas, group, false));
        toolRow.add(makeToolButton("Fill", Tool.FILL, canvas, group, false));
        toolRow.add(makeToolButton("Picker", Tool.PICKER, canvas, group, false));

        // Row 2: edit and file actions
        JButton undoBtn = new JButton("Undo");
        undoBtn.addActionListener(e -> canvas.undo());
        JButton redoBtn = new JButton("Redo");
        redoBtn.addActionListener(e -> canvas.redo());
        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> canvas.clearCanvas());

        JComboBox<Integer> scaleBox = new JComboBox<>(new Integer[]{1, 4, 8, 16, 32});
        scaleBox.setSelectedItem(16);
        JButton exportBtn = new JButton("Export PNG");
        exportBtn.addActionListener(e -> exportImage(grid, (Integer) scaleBox.getSelectedItem()));

        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionRow.add(undoBtn);
        actionRow.add(redoBtn);
        actionRow.add(clearBtn);
        actionRow.add(new JLabel("Scale:"));
        actionRow.add(scaleBox);
        actionRow.add(exportBtn);

        JPanel top = new JPanel(new GridLayout(0, 1)); // any rows, one column
        top.add(toolRow);
        top.add(actionRow);

        add(top, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);

        setupShortcuts(canvas);

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void setupShortcuts(CanvasPanel canvas) {
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();
        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx(); // Ctrl on Windows, Cmd on Mac

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, mask), "undo");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, mask), "redo");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, mask | InputEvent.SHIFT_DOWN_MASK), "redo");

        am.put("undo", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { canvas.undo(); }
        });
        am.put("redo", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { canvas.redo(); }
        });
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
            file = new File(file.getAbsolutePath() + ".png");
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