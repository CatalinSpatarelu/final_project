package com.example.final_project.exceptions;

public class ProductAlreadyExistException extends RuntimeException{
    public ProductAlreadyExistException(){
        super("Product already exist");
    }
}
