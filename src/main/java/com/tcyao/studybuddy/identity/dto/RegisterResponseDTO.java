package com.tcyao.studybuddy.identity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResponseDTO {
    private String email;
    private String password;
    private String displayName;
    private int age;


}
