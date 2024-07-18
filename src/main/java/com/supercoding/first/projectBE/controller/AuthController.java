package com.supercoding.first.projectBE.controller;

import com.supercoding.first.projectBE.config.jwt.TokenProvider;
import com.supercoding.first.projectBE.dto.LoginRequest;
import com.supercoding.first.projectBE.dto.LoginResponse;
import com.supercoding.first.projectBE.dto.LogoutRequest;
import com.supercoding.first.projectBE.dto.SignUpRequest;
import com.supercoding.first.projectBE.dto.SignUpResponse;
import com.supercoding.first.projectBE.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

  @Autowired
  private AuthService authService;
  private final TokenProvider tokenProvider;

  @Operation(summary = "회원가입")
  @PostMapping("/signup")
  public ResponseEntity<Map> signUp(@RequestBody SignUpRequest signUpRequest)
      throws BadRequestException {
    SignUpResponse signUpResponse = authService.signUp(signUpRequest);
    Map map = new HashMap();
    map.put("message","회원가입이 완료되었습니다.");
    return ResponseEntity.ok().body(map);
  }

  @Operation(summary = "로그아웃")
  @GetMapping("/logout")
  public ResponseEntity<Map> logout(@RequestBody LogoutRequest logoutRequest, HttpServletRequest request, HttpServletResponse response,@RequestHeader("Authorization") String token)
      throws BadRequestException, NotFoundException {

    String accessToken = tokenProvider.getAccessToken(token);

    if(!tokenProvider.validToken(accessToken) || !authService.logout(logoutRequest)){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    new SecurityContextLogoutHandler().logout(request, response,
        SecurityContextHolder.getContext().getAuthentication());

    Map map = new HashMap();
    map.put("message","로그아웃 되었습니다 ");
    return ResponseEntity.ok().body(map);
  }

  @Operation(summary = "로그인")
  @PostMapping("/login")
  public ResponseEntity<Map> login(@RequestBody LoginRequest loginRequest,
      HttpServletResponse httpServletResponse) throws Exception {

    String token = authService.login(loginRequest);
    httpServletResponse.setHeader("Authorization", "Bearer " + token);

    Map map = new HashMap();
    map.put("Authorization",token);
    map.put("message","로그인이 성공적으로 완료되었습니다.");
    return ResponseEntity.ok(map);
  }

}
