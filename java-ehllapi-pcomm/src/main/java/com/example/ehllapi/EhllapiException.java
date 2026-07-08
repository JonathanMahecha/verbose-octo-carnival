package com.example.ehllapi;

public final class EhllapiException extends RuntimeException {
    private final int function;
    private final int returnCode;

    public EhllapiException(String operation, int function, int returnCode) {
        super(operation + " failed (EHLLAPI function=" + function + ", returnCode=" + returnCode + ")");
        this.function = function;
        this.returnCode = returnCode;
    }

    public int function() { return function; }
    public int returnCode() { return returnCode; }
}

