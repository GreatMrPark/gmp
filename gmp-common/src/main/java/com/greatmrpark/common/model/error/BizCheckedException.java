package com.greatmrpark.common.model.error;

public class BizCheckedException extends AbstractException {
	
	private static final long serialVersionUID = 1L;
	
    public BizCheckedException(String code, String message) {
        super(code, message);
    }

    public BizCheckedException(String code, String message, Throwable err) {
        super(code, message, err);
    }

    public BizCheckedException(ErrCodable errCodable, String... args) {
        super(errCodable, args);
    }
}
