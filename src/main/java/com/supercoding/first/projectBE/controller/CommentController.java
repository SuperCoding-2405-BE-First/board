package com.supercoding.first.projectBE.controller;

import com.supercoding.first.projectBE.config.jwt.TokenProvider;
import com.supercoding.first.projectBE.dto.CommentAlterRequest;
import com.supercoding.first.projectBE.dto.CommentPostRequest;
import com.supercoding.first.projectBE.dto.CommentResponse;
import com.supercoding.first.projectBE.dto.LoginResponse;
import com.supercoding.first.projectBE.entity.Comment;
import com.supercoding.first.projectBE.exception.CommentNotFoundException;
import com.supercoding.first.projectBE.exception.PostNotFoundException;
import com.supercoding.first.projectBE.exception.UserNotEqualException;
import com.supercoding.first.projectBE.service.CommentService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

  private final CommentService commentService;
  private final TokenProvider tokenProvider;

  @GetMapping("/comments")
  public ResponseEntity<Map<String, List<CommentResponse>>> getPostIdComments(@RequestParam("post_id") Long postId, @RequestHeader("Authorization") String token) {
    String accessToken = tokenProvider.getAccessToken(token);

    if(!tokenProvider.validToken(accessToken)){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    List<CommentResponse> comments = commentService.getPostIdComments(postId);
    Map map = new HashMap();
    map.put("comments", comments);
    return ResponseEntity.ok().body(map);
  }

  @PostMapping("/comments")
  public ResponseEntity<Map> createComment(@RequestBody CommentPostRequest commentPostRequest, @RequestHeader("Authorization") String token)
      throws PostNotFoundException {

    String accessToken = tokenProvider.getAccessToken(token);

    if(!tokenProvider.validToken(accessToken)){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Long userId = tokenProvider.getUserId(accessToken);

    CommentResponse response = commentService.createComment(commentPostRequest, userId);
    Map map = new HashMap();
    map.put("message","댓글이 성공적으로 작성되었습니다.");
    return new ResponseEntity<>(map, HttpStatus.CREATED);

  }


  @PutMapping("/comments/{comment_id}")
  public ResponseEntity<Map> updateComment(@PathVariable Long comment_id,
                                                       @RequestBody CommentAlterRequest commentAlterRequest, @RequestHeader("Authorization") String token)
      throws CommentNotFoundException, UserNotEqualException {

    String accessToken = tokenProvider.getAccessToken(token);
    if(!tokenProvider.validToken(accessToken)){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Long userId = tokenProvider.getUserId(accessToken);
    commentService.updateComment(commentAlterRequest, userId, comment_id);
    Map map = new HashMap();
    map.put("message","댓글이 성공적으로 수정되었습니다.");
    return ResponseEntity.ok().body(map);

  }

  @DeleteMapping("comments/{comment_id}")
  public ResponseEntity<Void> deleteComment(@PathVariable Long comment_id, @RequestHeader("Authorization") String token)
      throws CommentNotFoundException, UserNotEqualException {

    String accessToken = tokenProvider.getAccessToken(token);
    if(!tokenProvider.validToken(accessToken)){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    Long userId = tokenProvider.getUserId(accessToken);

    if (!commentService.deleteComment(comment_id, userId)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
