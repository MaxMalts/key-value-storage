package org.maxmalts.key_value_storage_database;

public class MalformedDataException extends RuntimeException {
    public MalformedDataException() {
    }

    public MalformedDataException(String message) {
        super(message);
    }

    public MalformedDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedDataException(Throwable cause) {
        super(cause);
    }
}
