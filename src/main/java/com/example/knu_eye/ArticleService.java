package com.example.knu_eye;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ArticleService {

    // DB 역할을 하는 임시 저장소 (120번부터 시작)
    private Map<String, ArticleDto.Response> articleDb = new LinkedHashMap<>();
    private long idAutoIncrement = 120L;

    // CREATE (생성)
    public ArticleDto.Response createArticle(ArticleDto.Request request) {
        String newId = String.valueOf(++idAutoIncrement);

        ArticleDto.Response newArticle = ArticleDto.Response.builder()
                .id(newId)
                .title(request.getTitle())
                .contents(request.getContents())
                .author(request.getAuthor())
                .originalLink(request.getOriginalLink())
                .imageUrl(request.getImageUrl())
                .views(0) // 새 글은 조회수 0
                .uploadedAt(request.getUploadedAt())
                .deadline(request.getDeadline())
                .category(request.getCategory())
                .build();

        articleDb.put(newId, newArticle);
        return newArticle;
    }

    // READ (단건 조회)
    public ArticleDto.Response getArticle(String id) {
        if (!articleDb.containsKey(id)) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다.");
        }
        return articleDb.get(id);
    }

    // READ (목록 조회)
    public ArticleDto.ListResponse getArticles(String keyword, String category, int size, String lastId) {
        return ArticleDto.ListResponse.builder()
                .items(new ArrayList<>(articleDb.values()))
                .nextCursor(3L) // 명세서 예시에 맞춘 임시 커서
                .hasNext(true)
                .build();
    }

    // UPDATE (수정 - PATCH 방식)
    public ArticleDto.Response updateArticle(String id, ArticleDto.Request request) {
        if (!articleDb.containsKey(id)) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다.");
        }

        ArticleDto.Response existing = articleDb.get(id);

        // PATCH는 새로 들어온 값만 수정하고, 나머지는 기존 값을 유지합니다.
        ArticleDto.Response updatedArticle = ArticleDto.Response.builder()
                .id(id)
                .title(request.getTitle() != null ? request.getTitle() : existing.getTitle())
                .contents(request.getContents() != null ? request.getContents() : existing.getContents())
                .author(request.getAuthor() != null ? request.getAuthor() : existing.getAuthor())
                .originalLink(request.getOriginalLink() != null ? request.getOriginalLink() : existing.getOriginalLink())
                .imageUrl(request.getImageUrl() != null ? request.getImageUrl() : existing.getImageUrl())
                .views(existing.getViews())
                .uploadedAt(request.getUploadedAt() != null ? request.getUploadedAt() : existing.getUploadedAt())
                .deadline(request.getDeadline() != null ? request.getDeadline() : existing.getDeadline())
                .category(request.getCategory() != null ? request.getCategory() : existing.getCategory())
                .build();

        articleDb.put(id, updatedArticle);
        return updatedArticle;
    }

    // DELETE (삭제)
    public void deleteArticle(String id) {
        if (!articleDb.containsKey(id)) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다.");
        }
        articleDb.remove(id);
    }
}