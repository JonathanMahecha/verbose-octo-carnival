package com.example.ehllapi;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EhllapiSessionTest {
    @Test
    void connectsReadsWritesAndDisconnects() {
        FakeGateway gateway = new FakeGateway("MENU PRINCIPAL");
        try (var session = EhllapiSession.connect(gateway, "A", new ScreenSize(2, 20),
                StandardCharsets.US_ASCII)) {
            assertTrue(session.readScreen().contains("MENU PRINCIPAL"));
            session.write(new ScreenPosition(2, 1), "12");
            session.sendEnter();
        }
        assertEquals(List.of(1, 8, 15, 3, 2), gateway.functions);
    }

    @Test
    void waitsForExpectedText() {
        FakeGateway gateway = new FakeGateway("LISTO");
        try (var session = EhllapiSession.connect(gateway, "A", new ScreenSize(1, 10),
                StandardCharsets.US_ASCII)) {
            assertTrue(session.waitForText("LISTO", Duration.ofSeconds(1)).contains("LISTO"));
        }
    }

    private static final class FakeGateway implements EhllapiGateway {
        final List<Integer> functions = new ArrayList<>();
        final byte[] screen;

        FakeGateway(String text) {
            screen = new byte[40];
            java.util.Arrays.fill(screen, (byte) ' ');
            byte[] value = text.getBytes(StandardCharsets.US_ASCII);
            System.arraycopy(value, 0, screen, 0, value.length);
        }

        @Override
        public EhllapiCall call(int function, byte[] data, int length, int position) {
            functions.add(function);
            if (function == 8) System.arraycopy(screen, position - 1, data, 0, length);
            if (function == 15) System.arraycopy(data, 0, screen, position - 1, length);
            return new EhllapiCall(data, length, position, 0);
        }
    }
}

