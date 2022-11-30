package com.devmeggie.blog_app.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UpLoadPostDto {
    private String title;
    private String content;
    private String imageUrl;
}
