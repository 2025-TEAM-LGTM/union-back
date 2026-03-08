package com.union.demo.global.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

//성공 응답을 만드는 공통 DTO
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
    private int status; //200, 201
    private String msg; // OK
    private T data; //응답 데이터, 없으면 null

    //201
    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(201, "OK", data);
    }

    //200 성공 응답 with data
    public static <T> ApiResponse<T> ok(T data){
        return ApiResponse.<T> builder()
                .status(HttpStatus.OK.value())
                .msg("OK")
                .data(data)
                .build();
    }

    //200 성공 응답 without data
    public static ApiResponse<Void> ok(){
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .msg("OK")
                .data(null)
                .build();
    }
}
