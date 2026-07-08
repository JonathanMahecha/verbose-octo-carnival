package com.example.ehllapi;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.ptr.IntByReference;

/** Native entry point exported by IBM Personal Communications. */
interface EhllapiLibrary extends StdCallLibrary {
    EhllapiLibrary INSTANCE = Native.load("PCSHLL32", EhllapiLibrary.class);

    void hllapi(IntByReference function, byte[] data,
                IntByReference length, IntByReference returnCode);
}

