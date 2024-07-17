package com.supercoding.first.projectBE.service;

import com.supercoding.first.projectBE.dto.CommentAlterRequest;
import com.supercoding.first.projectBE.dto.CommentPostRequest;
import com.supercoding.first.projectBE.dto.CommentResponse;
import com.supercoding.first.projectBE.entity.Comment;
import com.supercoding.first.projectBE.entity.Post;
import com.supercoding.first.projectBE.entity.User;
import com.supercoding.first.projectBE.exception.CommentNotFoundException;
import com.supercoding.first.projectBE.exception.PostNotFoundException;
import com.supercoding.first.projectBE.exception.UserNotEqualException;
import com.supercoding.first.projectBE.repository.CommentRepository;
import com.supercoding.first.projectBE.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
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

  public List<CommentResponse> getPostIdComments(Long postId) {
    return commentRepository.findPostIdBy(postId);
  }

  public CommentResponse createComment(CommentPostRequest request, Long userId)
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
        .author(existUser.getAuthor())
        .build();

    comment = commentRepository.save(comment);
    return new CommentResponse(comment);

  }

  public Comment updateComment(CommentAlterRequest request, Long userId, Long commentId)
      throws CommentNotFoundException, UserNotEqualException {

    Comment existComment = commentRepository.findById(commentId).orElse(null);
    User existUser = userRepository.findByUserId(userId);
    if (existComment == null) {
      throw new CommentNotFoundException("댓글을 찾을 수 없습니다.");
    }
    if (existComment.getUser() != existUser) {
      throw new UserNotEqualException("본인이 작성한 댓글만 수정이 가능합니다.");
    }

    existComment.setContent(request.getContent());
    return commentRepository.save(existComment);

  }

  public boolean deleteComment(Long commentId, Long userId)
      throws CommentNotFoundException, UserNotEqualException {
    Comment existComment = commentRepository.findById(commentId).orElse(null);
    User user = userRepository.findByUserId(userId);
    if (existComment == null) {
      throw new CommentNotFoundException("댓글을 찾을 수 없습니다.");
    }
    if (existComment.getUser() != user) {
      throw new UserNotEqualException("본인이 작성한 댓글만 삭제가 가능합니다.");
    }
    commentRepository.delete(existComment);
    return true;
  }

}
