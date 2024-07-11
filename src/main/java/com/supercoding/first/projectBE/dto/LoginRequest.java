package com.supercoding.first.projectBE.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    private String email;

    private String password;

}
