package com.supercoding.first.projectBE.controller;



import com.supercoding.first.projectBE.config.jwt.TokenProvider;
import com.supercoding.first.projectBE.dto.PostRequest;
import com.supercoding.first.projectBE.dto.PostResponse;
import com.supercoding.first.projectBE.entity.Post;
import com.supercoding.first.projectBE.exception.PostNotFoundException;
import com.supercoding.first.projectBE.repository.PostRepository;
import com.supercoding.first.projectBE.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final TokenProvider tokenProvider;

    @Operation(summary = "게시물 생성", description = "제목 및 내용을 받아 인증된 사용자의 게시물 생성")
    @ApiResponse(responseCode = "200", description = "게시물이 성공적으로 작성 되었습니다 메세지 출력")
    @PostMapping("/posts")
    public ResponseEntity<Map> createPost(@RequestBody PostRequest requestDto,@RequestHeader("Authorization") String token) {
        // 토큰 값에서 'Bearer ' 부분을 제거
        String jwtToken = token.substring(7);

        //토큰 유효성 검사
        if(!tokenProvider.validToken(jwtToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = tokenProvider.getEmail(jwtToken);
        postService.createPost(requestDto,email);
        Map map = new HashMap();
        map.put("message","게시물이 성공적으로 작성 되었습니다.");
        return ResponseEntity.ok().body(map);
    }


    @Operation(summary = "게시물 전체 조회", description = "전체 게시물 조회")
    @ApiResponse(responseCode = "200", description = "게시물 리스트 전체 리스트 조회")
    @GetMapping("/posts")
    public ResponseEntity<Map<String,List<PostResponse>>> getAllPosts(@RequestHeader("Authorization") String token){ // API 조회 예시에 맞게 맵에 리스트를 담아 리턴 하도록 수정
        String accessToken = tokenProvider.getAccessToken(token);

        if(!tokenProvider.validToken(accessToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<PostResponse> posts = postService.getAllPosts();
        Map postsMap = new HashMap();
        postsMap.put("posts",posts);
        return ResponseEntity.ok().body(postsMap);
    }


    @Operation(summary = "게시물 상세 조회", description = "게시물 아이디의 게시물 조회")
    @ApiResponse(responseCode = "200", description = "게시물 아이디의 게시물 조회")
    @GetMapping("/posts/{post_id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long post_id,@RequestHeader("Authorization") String token) {
        Post post = postService.getPostById(post_id);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().body(new PostResponse(post));
    }

    @Operation(summary = "게시물 수정", description = "게시물 아이디의 게시물 수정")
    @ApiResponse(responseCode = "200", description = "수정된 값 반환")
    @PutMapping("/posts/{post_id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long post_id,
            @RequestBody PostRequest postRequest,
            @RequestHeader("Authorization") String token
    ) throws PostNotFoundException {
        Post updatedPost = postService.updatePost(post_id, postRequest);
        if (updatedPost == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().body(new PostResponse(updatedPost));
    }

    @Operation(summary = "게시물 삭제", description = "게시물 아이디 기준 게시물 삭제")
    @ApiResponse(responseCode = "200", description = "게시물 삭제 메세지 출력")
    @DeleteMapping("/posts/{post_id}")
    public ResponseEntity<Map> deletePost(@PathVariable Long post_id,@RequestHeader("Authorization") String token) {
        String accessToken = tokenProvider.getAccessToken(token);

        if(!tokenProvider.validToken(accessToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean deleted = postService.deletePost(post_id);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Map map = new HashMap();
        map.put("message","게시물이 삭제 되었습니다.");
        return ResponseEntity.ok().body(map);
    }

    @Operation(summary = "게시물 검색 (author_email)", description = "작성자 메일을 입력받아 해당 게시물 조회")
    @ApiResponse(responseCode = "200", description = "검색어 메일의 작성자 게시물 리스트 조회 ")
    @GetMapping("/posts/search")
    public ResponseEntity<List<PostResponse>> getPostsByEmail(@RequestParam(name="author_email") String email, @RequestHeader("Authorization") String token) {
        String accessToken = tokenProvider.getAccessToken(token);

        if(!tokenProvider.validToken(accessToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<PostResponse> list = postService.getPostsByUserEmail(email);
        return ResponseEntity.ok().body(list);
    }

}