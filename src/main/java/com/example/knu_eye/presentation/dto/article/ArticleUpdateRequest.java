package com.example.knu_eye.presentation.dto.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class ArticleUpdateRequest {

    private String title;
    private String contents;
    private String summary;
    private String author;

    @JsonProperty("original_link")
    private String originalLink;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("uploaded_at")
    private ZonedDateTime uploadedAt;

    private ZonedDateTime deadline;
    private String category;
}
