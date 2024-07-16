package com.supercoding.first.projectBE.dto;

import com.supercoding.first.projectBE.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {

  private Long commentId;
  private String content;

  public CommentResponse(Comment comment) {
    this.commentId = comment.getCommentId();
    this.content = comment.getContent();
  }
}
