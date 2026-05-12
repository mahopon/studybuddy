package com.tcyao.studybuddy.identity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequestDTO {
    private String oldPassword;
    private String newPassword;
}
