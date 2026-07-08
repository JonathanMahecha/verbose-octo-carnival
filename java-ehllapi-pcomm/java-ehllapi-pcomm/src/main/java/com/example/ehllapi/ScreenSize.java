package com.example.ehllapi;

public final class ScreenSize {
    public static final ScreenSize DEFAULT_5250 = new ScreenSize(24, 80);
    private final int rows;
    private final int columns;

    public ScreenSize(int rows, int columns) {
        if (rows < 1 || columns < 1) throw new IllegalArgumentException("Invalid screen size");
        this.rows = rows;
        this.columns = columns;
    }

    public int rows() { return rows; }
    public int columns() { return columns; }
    public int length() { return Math.multiplyExact(rows, columns); }
}
