package com.tcyao.studybuddy.identity.exceptions;

public class EmailInUseException extends RuntimeException {
    public EmailInUseException() {
        super("Email is already in use");
    }
}
