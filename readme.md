# Pixel Art Editor

A simple pixel art editor built with **Java Swing**. Draw on a 32×32 grid, fill regions, undo your mistakes, and export your artwork as a crisp, scaled PNG with a transparent background.

## Features

- **Four tools:** Pencil, eraser, flood-fill bucket, and colour picker.
- **Any colour:** Full colour chooser with a live colour swatch.
- **Transparency:** Empty cells display a checkerboard, and exports preserve real transparency.
- **Undo / Redo:** Step backwards and forwards through your edits (up to 50 steps).
- **PNG export:** Save at 1x, 4x, 8x, 16x, or 32x with sharp, unblurred pixels.
- **Keyboard shortcuts:** Ctrl+Z to undo, Ctrl+Y or Ctrl+Shift+Z to redo (Cmd on macOS).

## Getting Started

### Requirements

- JDK 17 or newer (check using `java -version`).
- Gradle is included through the Gradle Wrapper, so there is nothing else to install.

### Run the Application

Clone the repository:

```bash
git clone https://github.com/YOUR-USERNAME/pixel-art-editor.git
cd pixel-art-editor
```

Run the application:

**Linux / macOS / Git Bash:**
```bash
./gradlew run
```

**Windows PowerShell / Command Prompt:**
```powershell
.\gradlew.bat run
```

### Run the Tests

```bash
./gradlew test
```

## How to Use

| Action | How |
|---|---|
| Draw | Select Pencil, then click or drag on the canvas. |
| Erase | Select Eraser and click or drag to make cells transparent. |
| Fill an area | Select Fill and click inside a region. |
| Copy a colour | Select Picker and click a painted cell. |
| Change colour | Click Pick Colour. |
| Undo / Redo | Use the Undo / Redo buttons or Ctrl+Z / Ctrl+Y. |
| Clear everything | Click Clear (this action can also be undone). |
| Save your art | Choose a scale, click Export PNG, and select a save location. |

## Project Structure

```text
pixel-art-editor/
│
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── Main.java
│   │       │
│   │       ├── model
