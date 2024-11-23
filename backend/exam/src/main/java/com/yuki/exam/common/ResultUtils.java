package com.yuki.exam.common;

public class ResultUtils {

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    public static <T> BaseResponse<T> error(String message) {
        return new BaseResponse<>(1, null, message);
    }
}
