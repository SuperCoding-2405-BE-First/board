package com.supercoding.first.projectBE.controller;

import com.supercoding.first.projectBE.config.jwt.TokenProvider;
import com.supercoding.first.projectBE.dto.CommentRequest;
import com.supercoding.first.projectBE.dto.CommentResponse;
import com.supercoding.first.projectBE.entity.Comment;
import com.supercoding.first.projectBE.exception.CommentNotFoundException;
import com.supercoding.first.projectBE.exception.PostNotFoundException;
import com.supercoding.first.projectBE.exception.UserNotEqualException;
import com.supercoding.first.projectBE.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

  private final CommentService commentService;
  private final TokenProvider tokenProvider;

  @PostMapping("/comment")
  public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest commentRequest, @RequestHeader("Authorization") String token)
      throws PostNotFoundException {

    String accessToken = tokenProvider.getAccessToken(token);

    if(!tokenProvider.validToken(accessToken)){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Long userId = tokenProvider.getUserId(accessToken);

    CommentResponse response = commentService.createComment(commentRequest, userId);
    return new ResponseEntity<>(response, HttpStatus.CREATED);

  }


  @PutMapping("/comment/{commentId}")
  public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId,
      @RequestBody CommentRequest commentRequest, @RequestHeader("Authorization") String token)
      throws CommentNotFoundException, UserNotEqualException {

    String accessToken = tokenProvider.getAccessToken(token);
    if(!tokenProvider.validToken(accessToken)){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Long userId = tokenProvider.getUserId(accessToken);
    Comment response = commentService.updateComment(commentRequest, userId, commentId);
    return ResponseEntity.ok().body(new CommentResponse(response));

  }

  @DeleteMapping("comment/{commentId}")
  public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @RequestHeader("Authorization") String token)
      throws CommentNotFoundException, UserNotEqualException {

    String accessToken = tokenProvider.getAccessToken(token);
    if(!tokenProvider.validToken(accessToken)){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    Long userId = tokenProvider.getUserId(accessToken);

    if (!commentService.deleteComment(commentId, userId)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
