package com.mrochko.testingUA.exception;

import com.mrochko.testingUA.model.enums.ErrorType;

/**
 * @author Pavlo Mrochko
 */
public class EntityNotFoundException extends ServiceException {

    private static final String DEFAULT_MESSAGE = "Entity is not found";

    public EntityNotFoundException(){
        super(DEFAULT_MESSAGE);
    }

    public EntityNotFoundException(String message){
        super(message);
    }

    @Override
    public ErrorType getErrorType(){
        return ErrorType.VALIDATION_ERROR_TYPE;
    }

}
