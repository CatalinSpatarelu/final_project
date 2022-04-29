package com.example.final_project.exceptions;

public class ClientAlreadyExistException extends RuntimeException{
    public ClientAlreadyExistException(){
        super("Client already exist");
    }
}
