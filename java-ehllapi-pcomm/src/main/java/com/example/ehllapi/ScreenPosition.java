package com.example.ehllapi;

/** One-based 5250 screen coordinates. */
public record ScreenPosition(int row, int column) {
    public ScreenPosition {
        if (row < 1 || column < 1) throw new IllegalArgumentException("Row and column start at 1");
    }

    public int toOffset(int columns) {
        if (column > columns) throw new IllegalArgumentException("Column exceeds screen width");
        return (row - 1) * columns + column;
    }
}

