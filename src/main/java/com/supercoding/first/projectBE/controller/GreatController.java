package com.supercoding.first.projectBE.controller;


import com.supercoding.first.projectBE.config.jwt.TokenProvider;
import com.supercoding.first.projectBE.dto.GreatRequest;
import com.supercoding.first.projectBE.entity.Great;
import com.supercoding.first.projectBE.service.GreatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GreatController {

    private final GreatService greatService;
    private final TokenProvider tokenProvider;

    @GetMapping("/great/check/{post_id}") // 좋아요 여부 확인
    public ResponseEntity<Boolean> greatCheck(@PathVariable Long post_id,@RequestHeader("Authorization") String token ){
        // 토큰 값에서 'Bearer ' 부분을 제거
        String jwtToken = token.substring(7);

        //토큰 유효성 검사
        if(!tokenProvider.validToken(jwtToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = tokenProvider.getUserId(jwtToken);
        return ResponseEntity.ok(greatService.greatCheck(post_id,userId));
    }

    @PostMapping("/great") // 좋아요 추가 (표시)
    public ResponseEntity<Great> createGreat(@RequestBody GreatRequest greatRequest, @RequestHeader("Authorization") String token){
        // 토큰 값에서 'Bearer ' 부분을 제거
        String jwtToken = token.substring(7);

        //토큰 유효성 검사
        if(!tokenProvider.validToken(jwtToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = tokenProvider.getUserId(jwtToken);
        System.out.println(userId);
        Great great = greatService.createGreat(greatRequest.getPostId(),userId);
        return new ResponseEntity<>(great,HttpStatus.CREATED);
    }

    @DeleteMapping("/great/{great_id}") // 좋아요 삭제 (해제)
    public ResponseEntity<Void> deletePost(@PathVariable Long great_id) {
        boolean deleted = greatService.deletePost(great_id);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/great/count/{post_id}") // 좋아요 카운트 확인
    public ResponseEntity<Long> getPostGreatCount(@PathVariable Long post_id){
        return ResponseEntity.ok(greatService.getPostGreatCount(post_id));
    }

}
