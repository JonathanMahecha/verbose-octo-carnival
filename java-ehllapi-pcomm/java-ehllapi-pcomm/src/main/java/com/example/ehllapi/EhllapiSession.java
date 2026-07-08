package com.example.ehllapi;

import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/** High-level, mouse-free access to one Personal Communications presentation space. */
public final class EhllapiSession implements AutoCloseable {
    private final EhllapiGateway gateway;
    private final String shortName;
    private final ScreenSize screenSize;
    private final Charset charset;
    private boolean connected;

    private EhllapiSession(EhllapiGateway gateway, String shortName,
                           ScreenSize screenSize, Charset charset) {
        this.gateway = Objects.requireNonNull(gateway);
        this.shortName = validateShortName(shortName);
        this.screenSize = Objects.requireNonNull(screenSize);
        this.charset = Objects.requireNonNull(charset);
    }

    public static EhllapiSession connect(String shortName) {
        return connect(new NativeEhllapiGateway(), shortName, ScreenSize.DEFAULT_5250,
                Charset.forName("windows-1252"));
    }

    public static EhllapiSession connect(EhllapiGateway gateway, String shortName,
                                          ScreenSize size, Charset charset) {
        EhllapiSession session = new EhllapiSession(gateway, shortName, size, charset);
        byte[] name = shortName.getBytes(charset);
        session.requireSuccess("Connect", EhllapiFunction.CONNECT,
                gateway.call(EhllapiFunction.CONNECT.number, name, name.length, 0));
        session.connected = true;
        return session;
    }

    public String readScreen() { return read(1, screenSize.length()); }

    public String read(ScreenPosition position, int length) {
        return read(position.toOffset(screenSize.columns()), length);
    }

    public String read(int oneBasedOffset, int length) {
        ensureConnected();
        validateRange(oneBasedOffset, length);
        byte[] buffer = new byte[length];
        EhllapiCall call = gateway.call(EhllapiFunction.READ_SCREEN.number,
                buffer, length, oneBasedOffset);
        requireSuccess("Read screen", EhllapiFunction.READ_SCREEN, call);
        return new String(buffer, 0, length, charset);
    }

    public void write(ScreenPosition position, String value) {
        write(position.toOffset(screenSize.columns()), value);
    }

    public void write(int oneBasedOffset, String value) {
        ensureConnected();
        byte[] bytes = Objects.requireNonNull(value).getBytes(charset);
        validateRange(oneBasedOffset, bytes.length);
        requireSuccess("Write screen", EhllapiFunction.WRITE_SCREEN,
                gateway.call(EhllapiFunction.WRITE_SCREEN.number, bytes, bytes.length, oneBasedOffset));
    }

    public void writeField(ScreenPosition position, String value) {
        ensureConnected();
        byte[] bytes = Objects.requireNonNull(value).getBytes(charset);
        requireSuccess("Write field", EhllapiFunction.WRITE_FIELD,
                gateway.call(EhllapiFunction.WRITE_FIELD.number, bytes, bytes.length,
                        position.toOffset(screenSize.columns())));
    }

    public String readField(ScreenPosition position, int maximumLength) {
        ensureConnected();
        byte[] buffer = new byte[maximumLength];
        EhllapiCall call = gateway.call(EhllapiFunction.READ_FIELD.number, buffer,
                maximumLength, position.toOffset(screenSize.columns()));
        requireSuccess("Read field", EhllapiFunction.READ_FIELD, call);
        int used = call.length() > 0 && call.length() <= buffer.length ? call.length() : buffer.length;
        return stripTrailingSpaces(new String(buffer, 0, used, charset));
    }

    public void setCursor(ScreenPosition position) {
        ensureConnected();
        requireSuccess("Set cursor", EhllapiFunction.SET_CURSOR,
                gateway.call(EhllapiFunction.SET_CURSOR.number, new byte[1], 0,
                        position.toOffset(screenSize.columns())));
    }

    public void send(EhllapiKey key) { sendRawKey(Objects.requireNonNull(key).mnemonic()); }

    public void sendRawKey(String mnemonic) {
        ensureConnected();
        byte[] bytes = Objects.requireNonNull(mnemonic).getBytes(charset);
        requireSuccess("Send key", EhllapiFunction.SEND_KEY,
                gateway.call(EhllapiFunction.SEND_KEY.number, bytes, bytes.length, 0));
    }

    public void sendEnter() { send(EhllapiKey.ENTER); }

    public String waitForText(String expected, Duration timeout) {
        Objects.requireNonNull(expected);
        requirePositive(timeout);
        Instant deadline = Instant.now().plus(timeout);
        String screen;
        do {
            waitForHostCycle();
            screen = readScreen();
            if (screen.contains(expected)) return screen;
            sleepBriefly(deadline);
        } while (Instant.now().isBefore(deadline));
        throw new EhllapiTimeoutException("Text not found before timeout: " + expected);
    }

    public String waitUntilScreenChanges(String previousScreen, Duration timeout) {
        Objects.requireNonNull(previousScreen);
        requirePositive(timeout);
        Instant deadline = Instant.now().plus(timeout);
        String current;
        do {
            waitForHostCycle();
            current = readScreen();
            if (!current.equals(previousScreen)) return current;
            sleepBriefly(deadline);
        } while (Instant.now().isBefore(deadline));
        throw new EhllapiTimeoutException("Screen did not change before timeout");
    }

    public ScreenSize screenSize() { return screenSize; }
    public String shortName() { return shortName; }

    @Override
    public void close() {
        if (!connected) return;
        try {
            requireSuccess("Disconnect", EhllapiFunction.DISCONNECT,
                    gateway.call(EhllapiFunction.DISCONNECT.number, new byte[1], 0, 0));
        } finally {
            connected = false;
        }
    }

    private void waitForHostCycle() {
        EhllapiCall call = gateway.call(EhllapiFunction.WAIT.number, new byte[1], 0, 0);
        if (call.returnCode() != 0 && call.returnCode() != 1 && call.returnCode() != 4) {
            throw new EhllapiException("Wait", EhllapiFunction.WAIT.number, call.returnCode());
        }
    }

    private void requireSuccess(String operation, EhllapiFunction function, EhllapiCall call) {
        if (!call.successful()) throw new EhllapiException(operation, function.number, call.returnCode());
    }

    private void ensureConnected() {
        if (!connected) throw new IllegalStateException("EHLLAPI session is closed");
    }

    private void validateRange(int offset, int length) {
        if (offset < 1 || length < 0 || offset + length - 1 > screenSize.length())
            throw new IllegalArgumentException("Range is outside the presentation space");
    }

    private static String validateShortName(String value) {
        if (value == null || value.length() != 1 || !Character.isLetterOrDigit(value.charAt(0)))
            throw new IllegalArgumentException("PCOMM session short name must be one letter or digit");
        return value;
    }

    private static void requirePositive(Duration timeout) {
        if (timeout == null || timeout.isZero() || timeout.isNegative())
            throw new IllegalArgumentException("Timeout must be positive");
    }

    private static void sleepBriefly(Instant deadline) {
        long remaining = Duration.between(Instant.now(), deadline).toMillis();
        if (remaining <= 0) return;
        try {
            Thread.sleep(Math.min(100, remaining));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new EhllapiTimeoutException("Interrupted while waiting for host");
        }
    }

    private static String stripTrailingSpaces(String value) {
        int end = value.length();
        while (end > 0 && Character.isWhitespace(value.charAt(end - 1))) end--;
        return value.substring(0, end);
    }
}
