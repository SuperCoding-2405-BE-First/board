package com.supercoding.first.projectBE.service;

import com.supercoding.first.projectBE.entity.Great;
import com.supercoding.first.projectBE.repository.GreatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GreatService {
    private final GreatRepository greatRepository;

    public boolean greatCheck(Long postId , Long userId){
        return greatRepository.existsByPostIdAndUserId(postId,userId);
    }

    public Long getPostGreatCount(Long postId){
        return greatRepository.countByPostId(postId);
    }

    public boolean deletePostGreat(Long postId, Long userId ) {
        Great existingGreat = greatRepository.findByPostIdAndUserId(postId,userId);
        if (existingGreat != null) {
            greatRepository.delete(existingGreat);
            return true;
        }
        return false;
    }

    public Great createGreat(Long postId, Long userId) {
        Great great = new Great(postId,userId);
        return greatRepository.save(great);
    }
}
