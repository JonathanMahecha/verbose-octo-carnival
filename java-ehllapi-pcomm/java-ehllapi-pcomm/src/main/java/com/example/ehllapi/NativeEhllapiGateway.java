package com.example.ehllapi;

import com.sun.jna.ptr.IntByReference;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/** Thread-safe JNA adapter for PCSHLL32.DLL. */
public final class NativeEhllapiGateway implements EhllapiGateway {
    private static final ReentrantLock NATIVE_LOCK = new ReentrantLock(true);
    private final EhllapiLibrary library;

    public NativeEhllapiGateway() { this(EhllapiLibrary.INSTANCE); }

    NativeEhllapiGateway(EhllapiLibrary library) {
        this.library = Objects.requireNonNull(library);
    }

    @Override
    public EhllapiCall call(int function, byte[] data, int length, int position) {
        Objects.requireNonNull(data, "data");
        IntByReference fn = new IntByReference(function);
        IntByReference len = new IntByReference(length);
        IntByReference rc = new IntByReference(position);
        NATIVE_LOCK.lock();
        try {
            library.hllapi(fn, data, len, rc);
            return new EhllapiCall(data, len.getValue(), position, rc.getValue());
        } finally {
            NATIVE_LOCK.unlock();
        }
    }
}
