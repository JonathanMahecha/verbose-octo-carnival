package com.example.ehllapi;

enum EhllapiFunction {
    CONNECT(1), DISCONNECT(2), SEND_KEY(3), WAIT(4),
    SEARCH_SCREEN(6), READ_SCREEN(8), QUERY_SESSIONS(10),
    WRITE_SCREEN(15), QUERY_SESSION_STATUS(22),
    FIND_FIELD_POSITION(31), FIND_FIELD_LENGTH(32),
    WRITE_FIELD(33), READ_FIELD(34), SET_CURSOR(40);

    final int number;
    EhllapiFunction(int number) { this.number = number; }
}

