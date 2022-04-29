package com.example.final_project.exceptions;

public class PasswordNotPresentException extends RuntimeException{
    public PasswordNotPresentException(){
        super("Password not present");
    }
}
