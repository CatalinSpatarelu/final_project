package com.example.final_project.exceptions;


public class FavoriteItemNotFoundException extends RuntimeException{
    public FavoriteItemNotFoundException(){
        super("Favoite item cart not found");
    }
}
