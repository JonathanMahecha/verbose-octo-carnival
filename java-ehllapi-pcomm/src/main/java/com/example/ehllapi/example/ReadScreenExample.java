package com.example.ehllapi.example;

import com.example.ehllapi.EhllapiSession;

/** Start PCOMM session A before running this class. */
public final class ReadScreenExample {
    private ReadScreenExample() {}

    public static void main(String[] args) {
        String sessionName = args.length == 0 ? "A" : args[0];
        try (EhllapiSession session = EhllapiSession.connect(sessionName)) {
            String screen = session.readScreen();
            int width = session.screenSize().columns();
            for (int offset = 0; offset < screen.length(); offset += width) {
                System.out.println(screen.substring(offset, Math.min(offset + width, screen.length())));
            }
        }
    }
}

