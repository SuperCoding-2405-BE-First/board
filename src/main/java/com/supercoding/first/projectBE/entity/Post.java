package com.supercoding.first.projectBE.entity;

import com.supercoding.first.projectBE.dto.PostRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "post")
@EntityListeners(PostEntityListener.class)
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id")
  private Long postId;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @NotBlank
  @Size(max = 255)
  @Column(name = "title", nullable = false, length = 255)
  private String title;

  @Column(name = "content", columnDefinition = "text")
  private String content;

  @NotBlank
  @Size(max = 10)
  @Column(name = "author", nullable = false, length = 10)
  private String author;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @PrePersist
  public void prePersist() {
    this.createdAt = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  // 게시글 작성 : 생성자
  public Post(PostRequest requestDto, User user) {
    this.user = user;
    this.title = requestDto.getTitle();
    this.content = requestDto.getContent();
    this.author = user.getAuthor();
    this.createdAt = LocalDateTime.now();
  }

  // 게시글 수정
  public void update(PostRequest requestDto) {
    this.title = requestDto.getTitle();
    this.content = requestDto.getContent();
  }
}

