package com.example.ehllapi;

/** Common EHLLAPI mnemonic keys. Raw mnemonics can also be sent by the session. */
public enum EhllapiKey {
    ENTER("@E"), CLEAR("@C"), RESET("@R"), TAB("@T"), BACK_TAB("@B"),
    F1("@1"), F2("@2"), F3("@3"), F4("@4"), F5("@5"), F6("@6"),
    F7("@7"), F8("@8"), F9("@9"), F10("@a"), F11("@b"), F12("@c");

    private final String mnemonic;
    EhllapiKey(String mnemonic) { this.mnemonic = mnemonic; }
    public String mnemonic() { return mnemonic; }
}

