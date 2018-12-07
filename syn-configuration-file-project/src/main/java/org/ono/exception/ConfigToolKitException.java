package org.ono.exception;

/**
 * Created by ono on 2018/11/19.
 */
public class ConfigToolKitException extends Exception {

    public ConfigToolKitException() {
    }

    public ConfigToolKitException(String message) {
        super(message);
    }

    public ConfigToolKitException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigToolKitException(Throwable cause) {
        super(cause);
    }

    public ConfigToolKitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
