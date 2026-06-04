package com.example.knu_eye.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private String result;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("SUCCESS", data);
    }

    public static ApiResponse<ErrorMessage> fail(String message) {
        return new ApiResponse<>("FAIL", new ErrorMessage(message));
    }
}
