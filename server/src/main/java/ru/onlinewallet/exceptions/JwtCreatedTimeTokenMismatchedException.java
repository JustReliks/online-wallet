package ru.onlinewallet.exceptions;

public class JwtCreatedTimeTokenMismatchedException extends RuntimeException{
    public JwtCreatedTimeTokenMismatchedException() {
    }

    public JwtCreatedTimeTokenMismatchedException(String message) {
        super(message);
    }
}
