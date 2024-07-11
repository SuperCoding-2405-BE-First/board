package com.supercoding.first.projectBE.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SignUpRequest {

    private String author;

    private String email;

    private String password;

}
