package com.supercoding.first.projectBE.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 작성자 : Hwangseungmin
 * 수정 작업일시 : 2024.07.16
 * 작업 내용 : Entity 수정
 * 수정 원인 사항
 *  - 좋아요의 기능은 단순하며 , 입출력 및 확인 목적에서 사용자 및 게시물 객체에 대한 제약성이 너무 크게 작용함
 *  -  great 테이블 분리
 *  - 게시물 삭제 시 , 관련 좋아요는 삭제되도록 수동 설정
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "great")
public class Great implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "great_id")
    private Long greatId;


    @Column(name = "user_id")
    private Long userId;

    @Column(name = "post_id")
    private Long postId;

    public Great(Long postId , Long userId){ // 좋아요 추가 작업 사용
        this.postId = postId;
        this.userId = userId;
    }
}
