package com.Exception;


public class InvalidInputException extends  Exception{
    public String errorMessage;
    public InvalidInputException(String errorMessage)
    {
        this.errorMessage= errorMessage;
    }
    @Override
    public String getMessage(){
        return errorMessage;
    }
}