package com.supercoding.first.projectBE.service;

import com.supercoding.first.projectBE.config.auth.JwtUtil;
import com.supercoding.first.projectBE.dto.LoginRequest;
import com.supercoding.first.projectBE.dto.SignUpRequest;
import com.supercoding.first.projectBE.dto.SignUpResponse;
import com.supercoding.first.projectBE.entity.User;
import com.supercoding.first.projectBE.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignUpResponse signUpUser(SignUpRequest signup) {

        if(!checkUniqueEmail(signup.getEmail())) {
            //TODO : 이메일 중복체크
        }

        // 비밀번호 암호화
        String encryptPassword = passwordEncoder.encode(signup.getPassword());
        signup.setPassword(encryptPassword);

        // entity에 사용자가 입력한 값 setting
        User user = User.builder()
                .author(signup.getAuthor())
                .email(signup.getEmail())
                .password(signup.getPassword())
                .build();

        // 저장
        user = userRepository.save(user);

        // 저장된 user response에 담아서 전달
        return SignUpResponse.builder()
                .userId(user.getUserId())
                .author(user.getAuthor())
                .email(user.getEmail())
                .createdAt(convertTimestampToString(user.getCreatedAt()))
                .build();
    }

    @Transactional
    public String login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        User user = userRepository.findByUserEmail(email);
        if(user == null) {
            throw new UsernameNotFoundException("이메일이 존재하지 않습니다.");
        }

        // 암호화된 password를 디코딩한 값과 입력한 패스워드 값이 다르면 null 반환
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        return jwtUtil.createAccessToken(user);
    }


    public boolean checkUniqueEmail(String email) {
        User user = userRepository.findByUserEmail(email);
        return user == null;
    }


    public String convertTimestampToString(LocalDateTime localDateTime) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateFormat.format(localDateTime);
    }

}
