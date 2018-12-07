package org.ono.exception;

/**
 * Created by ono on 2018/11/19.
 */
public class InvalidPathException extends Exception {

    public InvalidPathException() {
    }

    public InvalidPathException(String message) {
        super(message);
    }

    public InvalidPathException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPathException(Throwable cause) {
        super(cause);
    }

    public InvalidPathException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
