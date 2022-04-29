package com.example.final_project.exceptions;

public class ClientNotFoundException extends RuntimeException{
    public ClientNotFoundException(){
        super("Client not found");
    }
}
