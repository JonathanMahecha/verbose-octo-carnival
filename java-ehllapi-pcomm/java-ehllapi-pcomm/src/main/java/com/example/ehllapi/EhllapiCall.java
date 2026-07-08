package com.example.ehllapi;

/** Immutable result of one native EHLLAPI invocation. */
public final class EhllapiCall {
    private final byte[] data;
    private final int length;
    private final int position;
    private final int returnCode;

    public EhllapiCall(byte[] data, int length, int position, int returnCode) {
        this.data = data;
        this.length = length;
        this.position = position;
        this.returnCode = returnCode;
    }

    public byte[] data() { return data; }
    public int length() { return length; }
    public int position() { return position; }
    public int returnCode() { return returnCode; }
    public boolean successful() { return returnCode == 0; }
}
