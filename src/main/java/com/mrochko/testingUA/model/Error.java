package com.mrochko.testingUA.model;

import com.mrochko.testingUA.model.enums.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Pavlo Mrochko
 */
@Data
@AllArgsConstructor
public class Error {

    private String message;
    private ErrorType errorType;
    private LocalDateTime timeStamp;

}
