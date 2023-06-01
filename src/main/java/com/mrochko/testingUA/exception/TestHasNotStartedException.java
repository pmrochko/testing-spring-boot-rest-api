package com.mrochko.testingUA.exception;

import com.mrochko.testingUA.model.enums.ErrorType;

/**
 * @author Pavlo Mrochko
 */
public class TestHasNotStartedException extends ServiceException {

    private static final String DEFAULT_MESSAGE = "The test has not been started";

    public TestHasNotStartedException(){
        super(DEFAULT_MESSAGE);
    }

    public TestHasNotStartedException(String message){
        super(message);
    }

    @Override
    public ErrorType getErrorType(){
        return ErrorType.PROCESSING_ERROR_TYPE;
    }

}
