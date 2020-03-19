package com.greatmrpark.helper.common.model.error;

public class ApiCheckedException extends AbstractException {
    public ApiCheckedException(String code, String message) {
        super(code, message);
    }

    public ApiCheckedException(String code, String message, Throwable err) {
        super(code, message, err);
    }

    public ApiCheckedException(ErrCodable errCodable, String... args) {
        super(errCodable, args);
    }
}
