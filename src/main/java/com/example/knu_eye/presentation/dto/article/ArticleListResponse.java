package com.example.knu_eye.presentation.dto.article;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ArticleListResponse {

    private List<ArticleDetailResponse> items;
    private Long nextCursor;
    private Boolean hasNext;
}
