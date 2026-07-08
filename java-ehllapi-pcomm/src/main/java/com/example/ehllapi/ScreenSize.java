package com.example.ehllapi;

public record ScreenSize(int rows, int columns) {
    public static final ScreenSize DEFAULT_5250 = new ScreenSize(24, 80);

    public ScreenSize {
        if (rows < 1 || columns < 1) throw new IllegalArgumentException("Invalid screen size");
    }

    public int length() { return Math.multiplyExact(rows, columns); }
}

