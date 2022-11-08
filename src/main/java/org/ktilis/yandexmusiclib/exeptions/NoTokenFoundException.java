package org.ktilis.yandexmusiclib.exeptions;

public class NoTokenFoundException extends Exception {
    public NoTokenFoundException() {
        super("No token was found in library. Please, log in.");
    }
}
