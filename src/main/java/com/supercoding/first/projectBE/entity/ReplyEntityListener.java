package com.supercoding.first.projectBE.entity;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class ReplyEntityListener {

    @PrePersist
    public void prePersist(Reply reply) {
        reply.setCreatedAt(LocalDateTime.now());
    }

    @PreUpdate
    public void preUpdate(Reply reply) {
        reply.setUpdatedAt(LocalDateTime.now());
    }
}
