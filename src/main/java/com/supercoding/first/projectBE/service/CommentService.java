package com.supercoding.first.projectBE.service;

import com.supercoding.first.projectBE.dto.CommentRequest;
import com.supercoding.first.projectBE.dto.CommentResponse;
import com.supercoding.first.projectBE.entity.Comment;
import com.supercoding.first.projectBE.entity.Post;
import com.supercoding.first.projectBE.entity.User;
import com.supercoding.first.projectBE.exception.PostNotFoundException;
import com.supercoding.first.projectBE.repository.CommentRepository;
import com.supercoding.first.projectBE.repository.PostRepository;
import com.supercoding.first.projectBE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

  private final UserRepository userRepository;
  private final CommentRepository commentRepository;
  private final PostService postService;

  public CommentResponse createComment(CommentRequest request, Long userId)
      throws PostNotFoundException {
    User existUser = userRepository.findByUserId(userId);
    Post existPost = postService.getPostById(request.getPostId());
    if (existUser == null) {
      throw new UsernameNotFoundException("유저가 존재하지 않습니다.");
    }

    if (existPost == null) {
      throw new PostNotFoundException("게시글이 존재하지 않습니다.");
    }

    Comment comment = Comment.builder()
        .user(existUser)
        .post(existPost)
        .content(request.getContent())
        .author(existUser.getUsername())
        .build();

    comment = commentRepository.save(comment);
    return new CommentResponse(comment);

  }

}
