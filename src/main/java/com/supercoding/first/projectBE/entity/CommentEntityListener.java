package com.supercoding.first.projectBE.entity;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class CommentEntityListener {

    @PrePersist
    public void prePersist(Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());
    }

    @PreUpdate
    public void preUpdate(Comment comment) {
        comment.setUpdatedAt(LocalDateTime.now());
    }
}