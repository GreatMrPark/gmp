package com.greatmrpark.common.model.error;

/**
 * The interface Err codable.
 */
public interface ErrCodable {
    /**
     * Gets err code.
     *
     * @return the err code
     */
    String getErrCode();

    /**
     * Gets message.
     *
     * @param args the args
     * @return the message
     */
    String getMessage(String... args);

    /**
     * 코드와 메세지를 함께 출력한다.
     * @param args
     * @return
     */
    String getCode(String... args);
}
