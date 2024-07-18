package com.supercoding.first.projectBE.dto;

import com.supercoding.first.projectBE.entity.Comment;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CommentResponse {

  private Long commentId;
  private String content;
  private String author;

  public CommentResponse(Comment comment) {
    this.commentId = comment.getCommentId();
    this.content = comment.getContent();
    this.author = comment.getAuthor();
  }
}
