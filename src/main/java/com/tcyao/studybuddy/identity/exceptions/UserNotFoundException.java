package com.tcyao.studybuddy.identity.exceptions;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    private static final String MESSAGE = "User with ID: %s not found";

    public UserNotFoundException(UUID id) {
        super(MESSAGE.formatted(id));
    }
}
