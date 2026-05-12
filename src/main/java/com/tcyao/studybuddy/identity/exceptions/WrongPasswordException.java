package com.tcyao.studybuddy.identity.exceptions;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super("Wrong password provided");
    }
}
