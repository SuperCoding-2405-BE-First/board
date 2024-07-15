package com.supercoding.first.projectBE.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class PostRequest {

    @NotBlank(message = "제목이 없습니다.")
    @Size(max = 255, message = "제목은 255자 이내 작성 가능합니다.")
    private String title;

    @NotBlank(message = "내용이 없습니다.")
    private String content;

    public PostRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
