package knueye.article_api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    // 1. 상태 (SUCCESS or FAIL)
    private String result;

    // 2. 실제 데이터 (성공 시에만 존재, 실패 시 null)
    private T data;

    // 3. 에러 정보 (실패 시에만 존재, 성공 시 null)
    private ErrorInfo error;

    // ✅ 성공했을 때 호출하는 메서드 (error 부분은 null로 비워둠)
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("SUCCESS", data, null);
    }

    // 🚨 실패했을 때 호출하는 메서드 (data 부분은 null로 비워두고, error에 내용 채움)
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>("FAIL", null, new ErrorInfo(message));
    }

    // 에러 상세 내용을 담을 내부 클래스
    @Getter
    @AllArgsConstructor
    public static class ErrorInfo {
        private String message;
        // 필요하다면 나중에 여기에 errorCode(예: "ERR-400") 등을 추가하기 쉽습니다.
    }
}