package com.example.ehllapi;

/** One-based 5250 screen coordinates. */
public final class ScreenPosition {
    private final int row;
    private final int column;

    public ScreenPosition(int row, int column) {
        if (row < 1 || column < 1) throw new IllegalArgumentException("Row and column start at 1");
        this.row = row;
        this.column = column;
    }

    public int row() { return row; }
    public int column() { return column; }

    public int toOffset(int columns) {
        if (column > columns) throw new IllegalArgumentException("Column exceeds screen width");
        return (row - 1) * columns + column;
    }
}
