package com.example.demo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 統一 API 回應格式
 * <p>
 * 所有 API 的標準回應格式，包含狀態碼、訊息、資料和時間戳記。
 * 提供靜態方法來建立成功和失敗的回應物件。
 * </p>
 * 
 * @param <T> 回應資料的類型
 * @author System Generated
 * @version 1.0
 * @since 2025-01-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    /**
     * 回應狀態碼
     */
    @JsonProperty("code")
    private Integer code;

    /**
     * 回應訊息
     */
    @JsonProperty("message")
    private String message;

    /**
     * 回應資料
     */
    @JsonProperty("data")
    private T data;

    /**
     * 時間戳記
     */
    @JsonProperty("timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    /**
     * 請求路徑
     */
    @JsonProperty("path")
    private String path;

    /**
     * 建立成功回應
     * 
     * @param data 回應資料
     * @param <T>  資料類型
     * @return 成功回應物件
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "操作成功", data, LocalDateTime.now(), null);
    }

    /**
     * 建立成功回應（自定義訊息）
     * 
     * @param data    回應資料
     * @param message 自定義訊息
     * @param <T>     資料類型
     * @return 成功回應物件
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(200, message, data, LocalDateTime.now(), null);
    }

    /**
     * 建立錯誤回應
     * 
     * @param code    錯誤碼
     * @param message 錯誤訊息
     * @param <T>     資料類型
     * @return 錯誤回應物件
     */
    public static <T> ApiResponse<T> error(Integer code, String message) {
        return new ApiResponse<>(code, message, null, LocalDateTime.now(), null);
    }

    /**
     * 建立錯誤回應（包含錯誤資料）
     * 
     * @param code    錯誤碼
     * @param message 錯誤訊息
     * @param data    錯誤資料
     * @param <T>     資料類型
     * @return 錯誤回應物件
     */
    public static <T> ApiResponse<T> error(Integer code, String message, T data) {
        return new ApiResponse<>(code, message, data, LocalDateTime.now(), null);
    }
}
