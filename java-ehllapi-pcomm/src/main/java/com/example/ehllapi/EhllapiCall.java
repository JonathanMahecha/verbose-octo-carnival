package com.example.ehllapi;

/** Immutable result of one native EHLLAPI invocation. */
public record EhllapiCall(byte[] data, int length, int position, int returnCode) {
    public boolean successful() { return returnCode == 0; }
}

