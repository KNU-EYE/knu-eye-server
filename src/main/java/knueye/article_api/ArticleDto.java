package knueye.article_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.ZonedDateTime;
import java.util.List;

public class ArticleDto {

    // 1. 요청(Request) 상자: 글 작성/수정 시 클라이언트가 보내는 데이터
    @Getter
    @Setter
    public static class Request {
        private String title;
        private String contents;
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

    // 2. 응답(Response) 상자: 서버가 단건 글 정보를 줄 때 사용하는 데이터
    @Getter
    @Builder
    public static class Response {
        private String id;
        private String title;
        private String contents;
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

    // 3. 리스트 응답(ListResponse) 상자: 게시글 목록을 줄 때 사용하는 데이터
    @Getter
    @Builder
    public static class ListResponse {
        private List<Response> items;
        private Long nextCursor;
        private Boolean hasNext;
    }
}