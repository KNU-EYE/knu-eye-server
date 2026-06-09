package com.example.knu_eye.presentation;

import com.example.knu_eye.application.ArticleService;
import com.example.knu_eye.global.dto.ApiResponse;
import com.example.knu_eye.presentation.dto.article.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ArticleController implements ArticleControllerDocs {

    private final ArticleService articleService;

    @Override
    @GetMapping("/api/articles")
    public ResponseEntity<ApiResponse<ArticleListResponse>> getArticles(
            @RequestParam(value = "search-keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "size", defaultValue = "100") int size,
            @RequestParam(value = "last-id", required = false) String lastId) {

        ArticleListResponse response = articleService.getArticles(keyword, category, size, lastId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Override
    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ApiResponse<ArticleDetailResponse>> getArticle(@PathVariable String id) {
        ArticleDetailResponse response = articleService.getArticle(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Override
    @PostMapping("/api/articles")
    public ResponseEntity<ApiResponse<ArticleCreateResponse>> createArticle(
            @Valid @RequestBody ArticleCreateRequest request) {

        ArticleCreateResponse response = articleService.createArticle(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @Override
    @PatchMapping("/api/articles/{id}")
    public ResponseEntity<ApiResponse<ArticleUpdateResponse>> updateArticle(
            @PathVariable String id,
            @RequestBody ArticleUpdateRequest request) {

        ArticleUpdateResponse response = articleService.updateArticle(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Override
    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteArticle(@PathVariable String id) {
        articleService.deleteArticle(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
