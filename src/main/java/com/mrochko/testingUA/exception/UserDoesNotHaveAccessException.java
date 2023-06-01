package com.mrochko.testingUA.exception;

import com.mrochko.testingUA.model.enums.ErrorType;

/**
 * @author Pavlo Mrochko
 */
public class UserDoesNotHaveAccessException extends ServiceException {

    private static final String DEFAULT_MESSAGE = "User is not authenticated";

    public UserDoesNotHaveAccessException(){
        super(DEFAULT_MESSAGE);
    }

    public UserDoesNotHaveAccessException(String message){
        super(message);
    }

    @Override
    public ErrorType getErrorType(){
        return ErrorType.SECURITY_ERROR_TYPE;
    }

}
