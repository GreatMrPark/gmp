package com.greatmrpark.common.model.error;

public class NotFoudExcption extends RuntimeException {

    private static final long serialVersionUID = 1642603080417304620L;

    public NotFoudExcption(String exception){

        super(exception);
    }
}
