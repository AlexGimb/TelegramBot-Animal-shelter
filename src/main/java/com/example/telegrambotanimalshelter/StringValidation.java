package com.example.telegrambotanimalshelter;

public class StringValidation {
    public static boolean validation(String str){
        return str != null && !str.isEmpty() && !str.isBlank();
    }
}
