package com.supercoding.first.projectBE.repository;

import com.supercoding.first.projectBE.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 추후 인증키(토큰) 에서 받아온 유저 Email 값을 기준으로 수정 예정
//    @Query("SELECT p FROM Post p WHERE p.user.email = :userEmail")
    List<Post> findByUserEmail(String userEmail);

}