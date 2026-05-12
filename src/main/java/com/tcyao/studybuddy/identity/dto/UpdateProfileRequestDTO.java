package com.tcyao.studybuddy.identity.dto;

import lombok.Getter;

import java.util.Optional;

@Getter
public class UpdateProfileRequestDTO {
    private String displayName;
    private int age;

    public Optional<String> getDisplayName() {
        return Optional.ofNullable(displayName);
    }

    public Optional<Integer> getAge() {
        return Optional.of(age);
    }
}
