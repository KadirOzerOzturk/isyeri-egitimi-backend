package com.isyeriegitimi.backend.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private String message;
    private boolean success;
    private T data;
    private int statusCode;


    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .data(data)
                .message(message)
                .success(true)
                .statusCode(200)
                .build();
    }


    public static <T> ApiResponse<T> error(String message, int statusCode) {
        return ApiResponse.<T>builder()
                .message(message)
                .success(false)
                .statusCode(statusCode)
                .build();
    }
}
