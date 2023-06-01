package com.mrochko.testingUA.exception;

import com.mrochko.testingUA.model.enums.ErrorType;

/**
 * @author Pavlo Mrochko
 */
public class IncorrectEnteredDataException extends ServiceException {

    private static final String DEFAULT_MESSAGE = "Incorrect entered data";

    public IncorrectEnteredDataException(){
        super(DEFAULT_MESSAGE);
    }

    public IncorrectEnteredDataException(String message){
        super(message);
    }

    @Override
    public ErrorType getErrorType(){
        return ErrorType.VALIDATION_ERROR_TYPE;
    }

}
