# Java EHLLAPI para IBM Personal Communications

Biblioteca Java para leer y escribir una sesión 5250 de IBM Personal Communications sin usar mouse, teclado ni capturas de pantalla. La comunicación con el emulador se realiza exclusivamente mediante Enhanced EHLLAPI (`PCSHLL32.DLL`) usando JNA.

## Requisitos

- Windows con IBM Personal Communications instalado.
- Una sesión 5250 iniciada y configurada con nombre corto, por ejemplo `A`.
- JDK 17 o superior y Maven 3.9 o superior.
- La arquitectura debe coincidir: JVM de 64 bits con PCOMM/EHLLAPI de 64 bits, o todo en 32 bits.

## Probar la conexión

```powershell
mvn test
mvn package
```

Ejecute `ReadScreenExample` desde su IDE, con la sesión `A` abierta. También puede pasar otra letra como primer argumento.

```java
try (EhllapiSession session = EhllapiSession.connect("A")) {
    String screen = session.readScreen();
    System.out.println(screen);

    session.writeField(new ScreenPosition(5, 20), "12");
    session.sendEnter();
    session.waitForText("RESULTADO", Duration.ofSeconds(15));
}
```

## Operaciones incluidas

- Conectar y desconectar una presentación PCOMM.
- Leer la pantalla completa o un rango.
- Escribir por posición o por campo.
- Leer campos.
- Mover el cursor.
- Enviar Enter, teclas de función y mnemónicos EHLLAPI.
- Esperar texto o un cambio de pantalla con timeout.
- Serializar llamadas nativas para evitar conflictos entre hilos.
- Sustituir la DLL por un gateway falso en pruebas unitarias.

## Notas importantes

- Las coordenadas son de base 1: la esquina superior izquierda es fila 1, columna 1.
- El tamaño predeterminado es 24x80. Para otro modelo use la sobrecarga `connect` con `ScreenSize`.
- No guarde usuarios o contraseñas en el código. Inyéctelos desde un gestor de secretos o variables protegidas.
- Adapte los textos, coordenadas y tiempos del ejemplo a las pantallas reales.
- IBM documenta códigos de retorno particulares por función; `EhllapiException` conserva la función y el código para diagnóstico.

## Referencias

- [Resumen oficial de funciones EHLLAPI](https://www.ibm.com/docs/en/personal-communications/16.0.0?topic=functions-summary-ehllapi)
- [Compilación, enlace y DLL de Enhanced EHLLAPI](https://www.ibm.com/docs/en/personal-communications/14.0.0?topic=programming-compiling-linking)

