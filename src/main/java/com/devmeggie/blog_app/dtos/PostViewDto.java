package com.devmeggie.blog_app.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostViewDto {
    private Long postId;
    private Long userId;
    private String title;
    private String content;
    private String imageUrl;

}
