package com.wawa_applications.wawa_tabor.exception;

public class TramNotFoundException extends RuntimeException {

    public TramNotFoundException(String line){
        super("Nie ma takiego autobusu o numerze: " + line);
    }
}
