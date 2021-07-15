package ru.onlinewallet.exceptions;

public class PasswordMatchException extends RuntimeException {
    public PasswordMatchException() {
    }

    public PasswordMatchException(String message) {
        super(message);
    }
}

