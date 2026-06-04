package com.example.knu_eye.application;

import com.example.knu_eye.domain.article.entity.Article;
import com.example.knu_eye.domain.article.entity.Category;
import com.example.knu_eye.domain.article.entity.CategoryEnum;
import com.example.knu_eye.domain.article.entity.Keyword;
import com.example.knu_eye.domain.article.repository.ArticleRepository;
import com.example.knu_eye.global.exception.ArticleNotFoundException;
import com.example.knu_eye.global.exception.InvalidRequestException;
import com.example.knu_eye.presentation.dto.article.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleDetailResponse getArticle(String id) {
        Article article = findByIdOrThrow(parseId(id));
        return toDetailResponse(article);
    }

    public ArticleListResponse getArticles(String keyword, String category, int size, String lastId) {
        Specification<Article> spec = (root, query, cb) -> cb.conjunction();

        if (keyword != null && !keyword.isBlank()) {
            String pattern = "%" + keyword.trim() + "%";
            spec = spec.and((root, query, cb) -> {
                query.distinct(true);
                Join<Article, Keyword> keywordJoin = root.join("keywords", JoinType.LEFT);
                return cb.or(
                        cb.like(root.get("title"), pattern),
                        cb.like(root.get("contents"), pattern),
                        cb.like(keywordJoin.get("name"), pattern)
                );
            });
        }
        if (category != null && !category.isBlank()) {
            CategoryEnum categoryEnum = parseCategory(category);
            spec = spec.and((root, query, cb) -> {
                query.distinct(true);
                Join<Article, Category> categoryJoin = root.join("categories");
                return cb.equal(categoryJoin.get("name"), categoryEnum);
            });
        }
        if (lastId != null && !lastId.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.lessThan(root.get("id"), parseId(lastId)));
        }

        PageRequest pageRequest = PageRequest.of(0, size + 1, Sort.by(Sort.Direction.DESC, "id"));
        List<Article> articles = articleRepository.findAll(spec, pageRequest).getContent();

        boolean hasNext = articles.size() > size;
        List<Article> page = hasNext ? articles.subList(0, size) : articles;
        Long nextCursor = hasNext ? page.get(page.size() - 1).getId() : null;

        return ArticleListResponse.builder()
                .items(page.stream().map(this::toDetailResponse).toList())
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }

    @Transactional
    public ArticleCreateResponse createArticle(ArticleCreateRequest request) {
        Article article = Article.builder()
                .title(request.getTitle())
                .contents(request.getContents())
                .summary(request.getSummary())
                .author(request.getAuthor())
                .originalLink(request.getOriginalLink())
                .imageUrl(request.getImageUrl())
                .views(0)
                .uploadedAt(toLocalDateTime(request.getUploadedAt()))
                .deadline(toLocalDateTime(request.getDeadline()))
                .build();

        if (request.getCategory() != null && !request.getCategory().isBlank()) {
            article.addCategory(parseCategory(request.getCategory()));
        }

        return toCreateResponse(articleRepository.save(article));
    }

    @Transactional
    public ArticleUpdateResponse updateArticle(String id, ArticleUpdateRequest request) {
        Article article = findByIdOrThrow(parseId(id));
        applyUpdate(article, request);
        return toUpdateResponse(article);
    }

    @Transactional
    public void deleteArticle(String id) {
        Article article = findByIdOrThrow(parseId(id));
        articleRepository.delete(article);
    }

    private void applyUpdate(Article article, ArticleUpdateRequest request) {
        article.updateTitle(request.getTitle());
        article.updateContents(request.getContents());
        article.updateSummary(request.getSummary());
        article.updateAuthor(request.getAuthor());
        article.updateOriginalLink(request.getOriginalLink());
        article.updateImageUrl(request.getImageUrl());
        article.updateUploadedAt(toLocalDateTime(request.getUploadedAt()));
        article.updateDeadline(toLocalDateTime(request.getDeadline()));

        if (request.getCategory() != null && !request.getCategory().isBlank()) {
            article.replaceCategories(List.of(parseCategory(request.getCategory())));
        }
    }

    private Article findByIdOrThrow(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(ArticleNotFoundException::new);
    }

    private Long parseId(String id) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new ArticleNotFoundException();
        }
    }

    private CategoryEnum parseCategory(String category) {
        try {
            return CategoryEnum.valueOf(category.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException();
        }
    }

    private String resolveCategory(Article article) {
        return article.getCategories().stream()
                .findFirst()
                .map(c -> c.getName().name())
                .orElse(null);
    }

    private ArticleDetailResponse toDetailResponse(Article article) {
        return ArticleDetailResponse.builder()
                .id(String.valueOf(article.getId()))
                .title(article.getTitle())
                .contents(article.getContents())
                .author(article.getAuthor())
                .originalLink(article.getOriginalLink())
                .imageUrl(article.getImageUrl())
                .views(article.getViews())
                .uploadedAt(toZonedDateTime(article.getUploadedAt()))
                .deadline(toZonedDateTime(article.getDeadline()))
                .category(resolveCategory(article))
                .build();
    }

    private ArticleCreateResponse toCreateResponse(Article article) {
        return ArticleCreateResponse.builder()
                .id(String.valueOf(article.getId()))
                .title(article.getTitle())
                .contents(article.getContents())
                .summary(article.getSummary())
                .author(article.getAuthor())
                .originalLink(article.getOriginalLink())
                .imageUrl(article.getImageUrl())
                .views(article.getViews())
                .uploadedAt(toZonedDateTime(article.getUploadedAt()))
                .deadline(toZonedDateTime(article.getDeadline()))
                .category(resolveCategory(article))
                .build();
    }

    private ArticleUpdateResponse toUpdateResponse(Article article) {
        return ArticleUpdateResponse.builder()
                .id(String.valueOf(article.getId()))
                .title(article.getTitle())
                .contents(article.getContents())
                .summary(article.getSummary())
                .author(article.getAuthor())
                .originalLink(article.getOriginalLink())
                .imageUrl(article.getImageUrl())
                .views(article.getViews())
                .uploadedAt(toZonedDateTime(article.getUploadedAt()))
                .deadline(toZonedDateTime(article.getDeadline()))
                .category(resolveCategory(article))
                .build();
    }

    private ZonedDateTime toZonedDateTime(java.time.LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.atZone(ZoneId.of("UTC"));
    }

    private java.time.LocalDateTime toLocalDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime == null ? null : zonedDateTime.toLocalDateTime();
    }
}
