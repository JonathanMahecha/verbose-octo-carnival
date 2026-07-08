package com.example.ehllapi;

/** Boundary around EHLLAPI, public to make business flows easy to unit-test. */
public interface EhllapiGateway {
    EhllapiCall call(int function, byte[] data, int length, int position);
}

