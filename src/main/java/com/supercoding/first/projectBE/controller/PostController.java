package com.supercoding.first.projectBE.controller;



import com.supercoding.first.projectBE.config.jwt.TokenProvider;
import com.supercoding.first.projectBE.dto.PostRequest;
import com.supercoding.first.projectBE.dto.PostResponse;
import com.supercoding.first.projectBE.entity.Post;
import com.supercoding.first.projectBE.repository.PostRepository;
import com.supercoding.first.projectBE.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;

    private final TokenProvider tokenProvider;

    @GetMapping("/posts") // 게시물 전체 조회
    public List<PostResponse> getAllPosts(){
        return postRepository.findAll().stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/posts") // 게시물 추가 API
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest requestDto,@RequestHeader("Authorization") String token) {
        // 토큰 값에서 'Bearer ' 부분을 제거
        String jwtToken = token.substring(7);

        //토큰 유효성 검사
        if(!tokenProvider.validToken(jwtToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = tokenProvider.getEmail(jwtToken);

        PostResponse responseDto =  postService.createPost(requestDto,email);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }


    // 게시물 상세 조회 API
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long postId) {
        Post post = postService.getPostById(postId);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().body(new PostResponse(post));
    }

    // 게시물 수정 API (제목과 내용만 수정 가능)
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long postId,
            @RequestBody PostRequest postRequest
    ) {
        Post updatedPost = postService.updatePost(postId, postRequest);
        if (updatedPost == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().body(new PostResponse(updatedPost));
    }

    // 게시물 삭제 API
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        boolean deleted = postService.deletePost(postId);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



    @GetMapping("/posts/search/{email}")
    public List<Post> getPostsByEmail(@PathVariable String email) {
        return postService.getPostsByUserEmail(email);
    }

}