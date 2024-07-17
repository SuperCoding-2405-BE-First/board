package com.supercoding.first.projectBE.repository;

import com.supercoding.first.projectBE.dto.CommentResponse;
import com.supercoding.first.projectBE.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  List<CommentResponse> findPostIdBy(Long postId);

}
