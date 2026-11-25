package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PixelGridTest {
    @Test
    void fillStopsAtWalls() {
        PixelGrid g = new PixelGrid(5);
        int black = 0xFF000000, red = 0xFFFF0000;
        for (int y = 0; y < 5; y++) g.set(2, y, black);   // vertical wall

        g.floodFill(0, 0, red);

        assertEquals(red, g.get(1, 4));                    // left side filled
        assertEquals(PixelGrid.BLANK, g.get(3, 0));        // right side untouched
        assertEquals(black, g.get(2, 2));                  // wall untouched
    }
}