package com.supercoding.first.projectBE.service;

import com.supercoding.first.projectBE.dto.PostRequest;
import com.supercoding.first.projectBE.dto.PostResponse;
import com.supercoding.first.projectBE.entity.Post;
import com.supercoding.first.projectBE.entity.User;
import com.supercoding.first.projectBE.repository.PostRepository;
import com.supercoding.first.projectBE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public PostResponse createPost(PostRequest createPost) {

      //  System.out.println("??2"+request.toString());
        // 토큰으로 유저 Entity 확인 으로 수정 예정
        User user = new User();
        user.setUserId(2L);
        user.setAuthor("test01");
        Post post = new Post(createPost,user);
        Post savePost = postRepository.save(post);
        return new PostResponse(savePost);
    }

    public Post updatePost(Long id, PostRequest updatedPost) {
        Post existingPost = postRepository.findById(id).orElse(null);
        if (existingPost != null) {
            existingPost.setTitle(updatedPost.getTitle());
            existingPost.setContent(updatedPost.getContent());
            return postRepository.save(existingPost);
        }
        return null;
    }

    public boolean deletePost(Long id) {
        Post existingPost = postRepository.findById(id).orElse(null);
        if (existingPost != null) {
            postRepository.delete(existingPost);
            return true;
        }
        return false;
    }

    public List<Post> getPostsByUserEmail(String email){
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return postRepository.findByUserUserId(user.getUserId());
        } else {
            // 사용자 이메일이 없는 경우 예외 처리
            throw new RuntimeException("User not found with email: " + email);
        }
    }

}
