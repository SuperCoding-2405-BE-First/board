package com.supercoding.first.projectBE.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpResponse {

    private Long userId;

    private String author;

    private String email;

    private String createdAt;
}
