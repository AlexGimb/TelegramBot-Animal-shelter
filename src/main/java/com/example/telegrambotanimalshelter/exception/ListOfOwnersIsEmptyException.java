package com.example.telegrambotanimalshelter.exception;

public class ListOfOwnersIsEmptyException extends RuntimeException{
    public ListOfOwnersIsEmptyException(String message) {
        super(message);
    }
}
