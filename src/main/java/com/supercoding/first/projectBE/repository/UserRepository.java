package com.supercoding.first.projectBE.repository;

import com.supercoding.first.projectBE.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserEmail(String userEmail);

}
