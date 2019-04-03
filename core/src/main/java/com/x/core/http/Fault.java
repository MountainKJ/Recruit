package com.x.core.http;

public class Fault extends RuntimeException{
    private String errMessage;
    private int status;

    public Fault(String errMessage, int status) {
        super(errMessage);
        this.status = status;
    }
}
