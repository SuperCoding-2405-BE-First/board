package com.supercoding.first.projectBE.service;

import com.supercoding.first.projectBE.config.jwt.TokenProvider;
import com.supercoding.first.projectBE.dto.LoginRequest;
import com.supercoding.first.projectBE.dto.SignUpRequest;
import com.supercoding.first.projectBE.dto.SignUpResponse;
import com.supercoding.first.projectBE.entity.User;
import com.supercoding.first.projectBE.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final TokenProvider tokenProvider;

  @Transactional
  public SignUpResponse signUp(SignUpRequest signup) throws BadRequestException {

    if (checkDuplicateEmail(signup.getEmail())) {
      throw new BadRequestException("중복된 이메일입니다.");
    }

    if (!checkPassword(signup.getPassword())) {
      throw new BadRequestException("비밀번호는 대소문자, 숫자를 포함한 5글자 이상이여야합니다.");
    }

    // 비밀번호 암호화
    String encryptPassword = signup.getPassword();
    signup.setPassword(encryptPassword);

    // entity에 사용자가 입력한 값 setting
    User user = User.builder()
        .author(signup.getAuthor())
        .email(signup.getEmail())
        .password(bCryptPasswordEncoder.encode(signup.getPassword()))
        .createdAt(LocalDateTime.now())
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
  public String login(LoginRequest loginRequest) throws Exception {

    String email = loginRequest.getEmail();
    String password = loginRequest.getPassword();
    Optional<User> optionalUser = userRepository.findByEmail(email);
    User user = userRepository.findByEmail(email).orElseThrow(NotFoundException::new);
    if (optionalUser.isEmpty()) {
      throw new BadRequestException("존재하지 않는 회원입니다.");
    }

    // 암호화된 password를 디코딩한 값과 입력한 패스워드 값이 다르면 null 반환
    if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
      throw new Exception("비밀번호가 일치하지 않습니다.");
    }

    return tokenProvider.generateToken(user, Duration.ofDays(1));
  }


  public String convertTimestampToString(LocalDateTime localDateTime) {
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return dateFormat.format(localDateTime);
  }

  private boolean checkDuplicateEmail(String email) {
    Optional<User> optionalUser = userRepository.findByEmail(email);
    return optionalUser.isPresent();
  }

  private boolean checkPassword(String password) {
    String regExp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{5,}$";
    return password.matches(regExp);
  }

}
