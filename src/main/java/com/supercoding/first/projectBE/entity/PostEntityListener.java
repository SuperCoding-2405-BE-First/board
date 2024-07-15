package com.supercoding.first.projectBE.entity;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

class PostEntityListener {

    @PrePersist
    public void prePersist(Post post) {
        post.setCreatedAt(LocalDateTime.now());
    }

    @PreUpdate
    public void preUpdate(Post post) {
        post.setUpdatedAt(LocalDateTime.now());
    }
}