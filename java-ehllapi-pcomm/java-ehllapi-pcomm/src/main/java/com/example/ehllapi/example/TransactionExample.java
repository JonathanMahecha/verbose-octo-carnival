package com.example.ehllapi.example;

import com.example.ehllapi.EhllapiSession;
import com.example.ehllapi.ScreenPosition;

import java.time.Duration;

/** Replace screen labels and coordinates with those from your real 5250 application. */
public final class TransactionExample {
    private TransactionExample() {}

    public static void main(String[] args) {
        try (EhllapiSession session = EhllapiSession.connect("A")) {
            session.waitForText("MENU PRINCIPAL", Duration.ofSeconds(15));
            session.writeField(new ScreenPosition(5, 20), "12");
            session.sendEnter();

            session.waitForText("CONSULTA CLIENTE", Duration.ofSeconds(15));
            session.writeField(new ScreenPosition(8, 25), "123456789");
            session.sendEnter();

            session.waitForText("RESULTADO", Duration.ofSeconds(15));
            String status = session.readField(new ScreenPosition(12, 30), 20);
            System.out.println("Estado: " + status);
        }
    }
}

