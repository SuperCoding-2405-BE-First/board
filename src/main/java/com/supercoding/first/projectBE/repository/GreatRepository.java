package com.supercoding.first.projectBE.repository;

import com.supercoding.first.projectBE.entity.Great;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GreatRepository extends JpaRepository<Great,Long> {
    Long countByPostId(Long PostId);

    boolean existsByPostIdAndUserId(Long postId,Long userId);

    Great findByPostIdAndUserId(Long postId,Long userId);

    void deleteAllByPostId(Long postId);
}
