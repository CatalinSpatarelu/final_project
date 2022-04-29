package com.example.final_project.exceptions;

public class ShopCartNotFoundException extends RuntimeException{
    public ShopCartNotFoundException(){
        super("Shop cart not found");
    }
}
