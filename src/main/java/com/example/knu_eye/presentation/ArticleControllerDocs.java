package com.example.knu_eye.presentation;

import com.example.knu_eye.global.dto.ApiResponse;
import com.example.knu_eye.presentation.dto.article.ArticleCreateRequest;
import com.example.knu_eye.presentation.dto.article.ArticleCreateResponse;
import com.example.knu_eye.presentation.dto.article.ArticleDetailResponse;
import com.example.knu_eye.presentation.dto.article.ArticleListResponse;
import com.example.knu_eye.presentation.dto.article.ArticleUpdateRequest;
import com.example.knu_eye.presentation.dto.article.ArticleUpdateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

@Tag(name = "Article", description = "공지사항 조회 및 관리 API")
public interface ArticleControllerDocs {

    @Operation(summary = "공지사항 목록 조회", description = "검색어와 카테고리로 공지사항을 필터링하고 커서 기반으로 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "공지사항 목록 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 카테고리 또는 요청값",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ResponseEntity<ApiResponse<ArticleListResponse>> getArticles(
            @Parameter(description = "제목, 본문 또는 키워드 검색어", example = "장학금")
            String keyword,
            @Parameter(
                    description = "공지사항 카테고리",
                    example = "SCHOLARSHIP",
                    schema = @Schema(
                            allowableValues = {
                                    "SCHOLARSHIP", "ABROAD", "JOB", "UNDERGRADUATE", "GRADUATE",
                                    "DORM", "CLUB", "CERTIFICATE", "OTHERS"
                            }
                    )
            )
            String category,
            @Parameter(description = "조회할 공지사항 수", example = "100")
            int size,
            @Parameter(description = "마지막으로 조회한 공지사항 ID", example = "100")
            String lastId
    );

    @Operation(summary = "공지사항 상세 조회", description = "공지사항 ID로 상세 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "공지사항 상세 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "존재하지 않거나 올바르지 않은 공지사항 ID",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ResponseEntity<ApiResponse<ArticleDetailResponse>> getArticle(
            @Parameter(description = "공지사항 ID", required = true, example = "1")
            String id
    );

    @Operation(summary = "공지사항 생성", description = "새 공지사항을 생성합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "공지사항 생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "필수값 누락 또는 잘못된 요청값",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ResponseEntity<ApiResponse<ArticleCreateResponse>> createArticle(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "생성할 공지사항 정보",
                    required = true
            )
            @Valid ArticleCreateRequest request
    );

    @Operation(summary = "공지사항 수정", description = "공지사항 ID에 해당하는 정보를 수정합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "공지사항 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "존재하지 않는 공지사항 또는 잘못된 요청값",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ResponseEntity<ApiResponse<ArticleUpdateResponse>> updateArticle(
            @Parameter(description = "공지사항 ID", required = true, example = "1")
            String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "수정할 공지사항 정보",
                    required = true
            )
            ArticleUpdateRequest request
    );

    @Operation(summary = "공지사항 삭제", description = "공지사항 ID에 해당하는 공지사항을 삭제합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "공지사항 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "존재하지 않거나 올바르지 않은 공지사항 ID",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    ResponseEntity<ApiResponse<Void>> deleteArticle(
            @Parameter(description = "공지사항 ID", required = true, example = "1")
            String id
    );
}
