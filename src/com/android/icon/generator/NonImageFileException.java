package com.android.icon.generator;

public class NonImageFileException extends Exception {

    public NonImageFileException() {
    }

    public NonImageFileException(String message) {
        super(message);
    }

    public NonImageFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonImageFileException(Throwable cause) {
        super(cause);
    }

    public NonImageFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
