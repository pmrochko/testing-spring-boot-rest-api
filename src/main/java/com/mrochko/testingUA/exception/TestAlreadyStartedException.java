package com.mrochko.testingUA.exception;

import com.mrochko.testingUA.model.enums.ErrorType;

/**
 * @author Pavlo Mrochko
 */
public class TestAlreadyStartedException extends ServiceException {

    private static final String DEFAULT_MESSAGE = "Some of the tests have already started";

    public TestAlreadyStartedException(){
        super(DEFAULT_MESSAGE);
    }

    public TestAlreadyStartedException(String message){
        super(message);
    }

    @Override
    public ErrorType getErrorType(){
        return ErrorType.PROCESSING_ERROR_TYPE;
    }

}
