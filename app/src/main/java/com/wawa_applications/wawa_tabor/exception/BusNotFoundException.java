package com.wawa_applications.wawa_tabor.exception;

public class BusNotFoundException extends RuntimeException {

    public BusNotFoundException(String line){
        super("Nie ma takiego autobusu o numerze: " + line);
    }
}
