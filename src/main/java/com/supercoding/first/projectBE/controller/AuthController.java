package com.supercoding.first.projectBE.controller;

import com.supercoding.first.projectBE.dto.LoginRequest;
import com.supercoding.first.projectBE.dto.LoginResponse;
import com.supercoding.first.projectBE.dto.SignUpRequest;
import com.supercoding.first.projectBE.dto.SignUpResponse;
import com.supercoding.first.projectBE.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

  @Autowired
  private AuthService authService;

  @Operation(summary = "회원가입")
  @PostMapping("/signUp")
  public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest)
      throws BadRequestException {
    SignUpResponse signUpResponse = authService.signUp(signUpRequest);
    return ResponseEntity.status(HttpStatus.OK).body(signUpResponse);
  }

  @Operation(summary = "로그아웃")
  @GetMapping("/logout")
  public String logout(HttpServletRequest request, HttpServletResponse response) {
    new SecurityContextLogoutHandler().logout(request, response,
        SecurityContextHolder.getContext().getAuthentication());
    return "success";
  }

  @Operation(summary = "로그인")
  @PostMapping("/login")
  public HttpEntity<?> login(@RequestBody LoginRequest loginRequest,
      HttpServletResponse httpServletResponse) throws Exception {

    String token = authService.login(loginRequest);
    httpServletResponse.setHeader("Authorization", "Bearer " + token);

    return new ResponseEntity<>(
        LoginResponse.builder().accessToken(token).build(), HttpStatus.OK
    );

  }

}
