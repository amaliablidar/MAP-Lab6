package com.Exception;

public class InputMismatchException extends java.util.InputMismatchException {
    public String errorMessage;
    public InputMismatchException(String errorMessage)
    {

        this.errorMessage=errorMessage;
    }
    @Override
    public String getMessage(){
        return errorMessage;
    }

}