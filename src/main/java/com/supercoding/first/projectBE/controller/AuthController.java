package com.supercoding.first.projectBE.controller;

import com.supercoding.first.projectBE.dto.LoginRequest;
import com.supercoding.first.projectBE.dto.SignUpRequest;
import com.supercoding.first.projectBE.dto.SignUpResponse;
import com.supercoding.first.projectBE.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(name = "/signUp")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        SignUpResponse signUpResponse = authService.signUpUser(signUpRequest);
        return ResponseEntity.status(HttpStatus.OK).body(signUpResponse);
    }

    @PostMapping(name = "/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

}
