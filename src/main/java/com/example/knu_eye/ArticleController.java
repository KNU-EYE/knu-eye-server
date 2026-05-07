package com.example.knu_eye;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    // 1. 목록 조회 (GET)
    @GetMapping
    public ResponseEntity<ApiResponse<ArticleDto.ListResponse>> getArticles(
            @RequestParam(value = "search-keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "size", defaultValue = "3") int size,
            @RequestParam(value = "last-id", required = false) String lastId) {

        ArticleDto.ListResponse responseData = articleService.getArticles(keyword, category, size, lastId);
        return ResponseEntity.ok(ApiResponse.success(responseData));
    }

    // 2. 단건 조회 (GET)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getArticle(@PathVariable String id) {
        try {
            ArticleDto.Response responseData = articleService.getArticle(id);
            return ResponseEntity.ok(ApiResponse.success(responseData));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.fail(e.getMessage()));
        }
    }

    // 3. 게시글 생성 (POST)
    @PostMapping
    public ResponseEntity<ApiResponse<ArticleDto.Response>> createArticle(
            @RequestBody ArticleDto.Request request) {

        ArticleDto.Response responseData = articleService.createArticle(request);
        return ResponseEntity.status(HttpStatus.CREATED) // 성공 시 상태 코드 201
                .body(ApiResponse.success(responseData));
    }

    // 4. 게시글 수정 (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateArticle(
            @PathVariable String id,
            @RequestBody ArticleDto.Request request) {
        try {
            ArticleDto.Response responseData = articleService.updateArticle(id, request);
            return ResponseEntity.ok(ApiResponse.success(responseData));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.fail(e.getMessage()));
        }
    }

    // 5. 게시글 삭제 (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteArticle(@PathVariable String id) {
        try {
            articleService.deleteArticle(id);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.fail(e.getMessage()));
        }
    }
}