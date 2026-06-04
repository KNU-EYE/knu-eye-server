package com.example.knu_eye.domain.article.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "articles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 256)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(length = 256)
    private String author;

    @Column(name = "original_link", columnDefinition = "TEXT")
    private String originalLink;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(nullable = false)
    private Integer views;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    private LocalDateTime deadline;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Keyword> keywords = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories = new ArrayList<>();

    @Builder
    private Article(
            String title,
            String contents,
            String summary,
            String author,
            String originalLink,
            String imageUrl,
            Integer views,
            LocalDateTime uploadedAt,
            LocalDateTime deadline
    ) {
        this.title = title;
        this.contents = contents;
        this.summary = summary;
        this.author = author;
        this.originalLink = originalLink;
        this.imageUrl = imageUrl;
        this.views = views != null ? views : 0;
        this.uploadedAt = uploadedAt;
        this.deadline = deadline;
    }

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void addKeyword(String name) {
        Keyword keyword = Keyword.builder()
                .name(name)
                .article(this)
                .build();
        keywords.add(keyword);
    }

    public void addCategory(CategoryEnum categoryEnum) {
        Category category = Category.builder()
                .name(categoryEnum)
                .article(this)
                .build();
        categories.add(category);
    }

    public void replaceCategories(List<CategoryEnum> categoryEnums) {
        categories.clear();
        if (categoryEnums != null) {
            categoryEnums.forEach(this::addCategory);
        }
    }

    public void replaceKeywords(List<String> keywordNames) {
        keywords.clear();
        if (keywordNames != null) {
            keywordNames.forEach(this::addKeyword);
        }
    }

    public void updateTitle(String title) {
        if (title != null) {
            this.title = title;
        }
    }

    public void updateContents(String contents) {
        if (contents != null) {
            this.contents = contents;
        }
    }

    public void updateSummary(String summary) {
        if (summary != null) {
            this.summary = summary;
        }
    }

    public void updateAuthor(String author) {
        if (author != null) {
            this.author = author;
        }
    }

    public void updateOriginalLink(String originalLink) {
        if (originalLink != null) {
            this.originalLink = originalLink;
        }
    }

    public void updateImageUrl(String imageUrl) {
        if (imageUrl != null) {
            this.imageUrl = imageUrl;
        }
    }

    public void updateUploadedAt(LocalDateTime uploadedAt) {
        if (uploadedAt != null) {
            this.uploadedAt = uploadedAt;
        }
    }

    public void updateDeadline(LocalDateTime deadline) {
        if (deadline != null) {
            this.deadline = deadline;
        }
    }
}
