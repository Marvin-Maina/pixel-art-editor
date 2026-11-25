package io;

import model.PixelGrid;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageExporter {

    /** Writes the grid as a PNG, with each cell enlarged to scale x scale pixels. */
    public static void exportPng(PixelGrid grid, File file, int scale) throws IOException {
        int side = grid.size * scale;
        BufferedImage img = new BufferedImage(side, side, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < side; y++) {
            for (int x = 0; x < side; x++) {
                img.setRGB(x, y, grid.get(x / scale, y / scale));
            }
        }

        if (!ImageIO.write(img, "png", file)) {
            throw new IOException("No PNG writer available");
        }
    }
}