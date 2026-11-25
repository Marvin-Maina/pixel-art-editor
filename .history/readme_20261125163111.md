Pixel Art Editor

A simple pixel art editor built with Java Swing. Draw on a 32×32 grid, fill regions, undo your mistakes, and export your artwork as a crisp, scaled PNG with a transparent background.

Pixel Art Editor screenshot

Features
Four tools: pencil, eraser, flood-fill bucket and colour picker
Any colour: full colour chooser with a live colour swatch
Transparency: empty cells show a checkerboard, and exports keep real transparency
Undo / redo: step backwards and forwards through your edits (up to 50 steps)
PNG export: save at 1x, 4x, 8x, 16x or 32x with sharp, unblurred pixels
Keyboard shortcuts: Ctrl+Z to undo, Ctrl+Y or Ctrl+Shift+Z to redo (Cmd on macOS)
Getting started
Requirements
JDK 17 or newer (check with java -version)

Gradle is included through the Gradle Wrapper, so there is nothing else to install.

Run it
bash
git clone https://github.com/YOUR-USERNAME/pixel-art-editor.git
cd pixel-art-editor
./gradlew run

On Windows PowerShell or Command Prompt, use gradlew.bat run (Git Bash accepts ./gradlew run).

Run the tests
bash
./gradlew test
How to use
Action	How
Draw	Select Pencil, then click or drag on the canvas
Erase	Select Eraser and click or drag, which makes cells transparent
Fill an area	Select Fill and click inside a region
Copy a colour	Select Picker and click a painted cell
Change colour	Click Pick colour
Undo / redo	Undo / Redo buttons, or Ctrl+Z / Ctrl+Y
Clear everything	Clear (this can be undone too)
Save your art	Choose a Scale, click Export PNG, pick a location
Project structure
src/
├── main/java/
│   ├── Main.java                  # Entry point, launches the window
│   ├── model/
│   │   ├── PixelGrid.java         # 2D array of ARGB colours, flood fill, snapshots
│   │   └── History.java           # Undo/redo stacks
│   ├── ui/
│   │   ├── MainFrame.java         # Window, toolbar, keyboard shortcuts
│   │   ├── CanvasPanel.java       # Draws the grid and handles mouse input
│   │   └── Tool.java              # Enum of available tools
│   └── io/
│       └── ImageExporter.java     # Writes the grid out as a scaled PNG
└── test/java/                     # JUnit tests for the model and exporter

The model package has no Swing code, which keeps the drawing logic easy to test on its own. The ui package only draws the model and edits it in response to the user.

How it works
Flood fill: a queue-based fill spreads outward from the clicked cell, recolouring connected cells of the same colour. A queue is used instead of recursion to avoid stack overflows on larger grids.
Undo / redo: before each stroke, a full copy of the grid is pushed onto an undo stack. At 32×32 that is only 1,024 ints, so snapshots are cheap and much simpler than tracking individual changes.
Crisp exports: scaling uses nearest-neighbour sampling (x / scale), so each cell becomes a sharp square rather than a blurry smear.
Transparency: cells store ARGB values, and a blank cell has alpha 0. The canvas paints a checkerboard first and each cell on top of it.
Built with
Java 17+
Java Swing / AWT
Gradle (via the wrapper)
JUnit 5
Ideas for the future
[ ] Save and load projects in a custom format
[ ] Zoom slider
[ ] Line, rectangle and circle tools
[ ] Colour palette presets
[ ] Mirror drawing mode for symmetrical sprites
[ ] Import an image and pixelate it
[ ] Animation frames exported as a GIF
Author

Made by Marvin as a learning project.

License

This project is licensed under the MIT License. See the LICENSE file for details, or choose another license when you create the repo on GitHub.