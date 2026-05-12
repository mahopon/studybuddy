package com.tcyao.studybuddy.identity.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class LoginRequestDTO {
    private UUID id;
    private String email;
    private String password;
}
