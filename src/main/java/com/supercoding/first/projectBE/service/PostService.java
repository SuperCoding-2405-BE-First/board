package com.supercoding.first.projectBE.service;

import com.supercoding.first.projectBE.dto.PostRequest;
import com.supercoding.first.projectBE.dto.PostResponse;
import com.supercoding.first.projectBE.entity.Post;
import com.supercoding.first.projectBE.entity.User;
import com.supercoding.first.projectBE.repository.GreatRepository;
import com.supercoding.first.projectBE.repository.PostRepository;
import com.supercoding.first.projectBE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final GreatRepository greatRepository;

    public List<PostResponse> getAllPosts() {
        return  postRepository.findAll().stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public PostResponse createPost(PostRequest createPost, String email) {
        Optional<User> user = userRepository.findByEmail(email);
        Post post;
        if(user.isPresent()){
            post = new Post(createPost,user.get());
        }else{
            throw new UsernameNotFoundException("해당 유저를 찾지 못함 ");
        }

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
            // 좋아요 삭제
            greatRepository.deleteAllByPostId(id);
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
