package com.example.knu_eye.presentation.dto.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Builder
public class ArticleCreateResponse {

    private String id;
    private String title;
    private String contents;
    private String summary;
    private String author;

    @JsonProperty("original_link")
    private String originalLink;

    @JsonProperty("image_url")
    private String imageUrl;

    private Integer views;

    @JsonProperty("uploaded_at")
    private ZonedDateTime uploadedAt;

    private ZonedDateTime deadline;
    private String category;
}
