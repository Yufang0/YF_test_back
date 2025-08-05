package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 員工未找到異常
 * <p>
 * 當根據指定條件無法找到員工時拋出此例外。
 * 此例外會自動回應 HTTP 404 Not Found 狀態碼。
 * </p>
 * 
 * @author System Generated
 * @version 1.0
 * @since 2025-01-15
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmployeeNotFoundException extends RuntimeException {

    private final String errorCode;

    /**
     * 建構函式
     * 
     * @param message 例外訊息
     */
    public EmployeeNotFoundException(String message) {
        super(message);
        this.errorCode = "EMPLOYEE_NOT_FOUND";
    }

    /**
     * 建構函式（包含錯誤碼）
     * 
     * @param message   例外訊息
     * @param errorCode 錯誤碼
     */
    public EmployeeNotFoundException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * 建構函式（包含原因）
     * 
     * @param message 例外訊息
     * @param cause   原始例外
     */
    public EmployeeNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "EMPLOYEE_NOT_FOUND";
    }

    /**
     * 獲取錯誤碼
     * 
     * @return 錯誤碼
     */
    public String getErrorCode() {
        return errorCode;
    }
}
