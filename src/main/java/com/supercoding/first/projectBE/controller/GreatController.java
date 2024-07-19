package com.supercoding.first.projectBE.controller;


import com.supercoding.first.projectBE.config.jwt.TokenProvider;
import com.supercoding.first.projectBE.dto.GreatRequest;
import com.supercoding.first.projectBE.entity.Great;
import com.supercoding.first.projectBE.service.GreatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GreatController {

    private final GreatService greatService;
    private final TokenProvider tokenProvider;
    @Operation(summary = "좋아요 확인", description = "게시물에 대한 좋아요 여부 확인")
    @ApiResponse(responseCode = "200", description = "true / false 반환")
    @GetMapping("/great/check/{post_id}")
    public ResponseEntity<Boolean> greatCheck(@PathVariable Long post_id,@RequestHeader("Authorization") String token ){
        String jwtToken = token.substring(7);// 토큰 값에서 'Bearer ' 부분을 제거

        //토큰 유효성 검사
        if(!tokenProvider.validToken(jwtToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = tokenProvider.getUserId(jwtToken);
        return ResponseEntity.ok(greatService.greatCheck(post_id,userId));
    }

    @Operation(summary = "좋아요 설정(추가)", description = "게시물에 대한 좋아요 설정(추가)")
    @ApiResponse(responseCode = "200", description = "생성 객체 및 , 201 생성코드 반환")
    @PostMapping("/great")
    public ResponseEntity<Great> createGreat(@RequestBody GreatRequest greatRequest, @RequestHeader("Authorization") String token){
        String jwtToken = token.substring(7);// 토큰 값에서 'Bearer ' 부분을 제거

        //토큰 유효성 검사
        if(!tokenProvider.validToken(jwtToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = tokenProvider.getUserId(jwtToken);
        Great great = greatService.createGreat(greatRequest.getPostId(),userId);
        return new ResponseEntity<>(great,HttpStatus.CREATED);
    }

    @Operation(summary = "좋아요 해제(삭제)", description = "게시물에 대한 좋아요 해제(삭제)")
    @ApiResponse(responseCode = "204", description = "204 No Content 상태 반환")
    @DeleteMapping("/great/{post_id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long post_id, @RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);// 토큰 값에서 'Bearer ' 부분을 제거

        //토큰 유효성 검사
        if(!tokenProvider.validToken(jwtToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = tokenProvider.getUserId(jwtToken);
        boolean deleted = greatService.deletePost(post_id,userId);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "좋아요 수 확인", description = "게시물에 대한 좋아요 개수 확인")
    @ApiResponse(responseCode = "200", description = "좋아요 수 반환")
    @GetMapping("/great/count/{post_id}")
    public ResponseEntity<Long> getPostGreatCount(@PathVariable Long post_id, @RequestHeader("Authorization") String token){
        String jwtToken = token.substring(7);// 토큰 값에서 'Bearer ' 부분을 제거

        //토큰 유효성 검사
        if(!tokenProvider.validToken(jwtToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().body(greatService.getPostGreatCount(post_id));
    }

}
