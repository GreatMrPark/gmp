package com.greatmrpark.web.common.model.error;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Abstract bot exception.
 */
@Getter
@Setter
public class AbstractException extends Exception {
    private String code;
    private String message;

    private AbstractException(String message) {
        super(message);
        this.message = message;
    }

    /**
     * Instantiates a new Abstract bot exception.
     *
     * @param code    the code
     * @param message the message
     */
    protected AbstractException(String code, String message) {
        this(message);
        this.code = code;
    }

    private AbstractException(String message, Throwable err) {
        super(message, err);
        this.message = message;
    }

    /**
     * Instantiates a new Abstract bot exception.
     *
     * @param code    the code
     * @param message the message
     * @param err     the err
     */
    protected AbstractException(String code, String message, Throwable err) {
        this(message, err);
        this.code = code;
    }

    protected AbstractException(ErrCodable errCodable, String...args) {
        this(errCodable.getErrCode(), errCodable.getMessage(args));
    }
}
