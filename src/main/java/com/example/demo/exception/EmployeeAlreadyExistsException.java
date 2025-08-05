package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 員工已存在異常
 * <p>
 * 當嘗試建立已存在的員工（如員工編號重複）時拋出此例外。
 * 此例外會自動回應 HTTP 409 Conflict 狀態碼。
 * </p>
 * 
 * @author System Generated
 * @version 1.0
 * @since 2025-01-15
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class EmployeeAlreadyExistsException extends RuntimeException {

    private final String errorCode;

    /**
     * 建構函式
     * 
     * @param message 例外訊息
     */
    public EmployeeAlreadyExistsException(String message) {
        super(message);
        this.errorCode = "EMPLOYEE_ALREADY_EXISTS";
    }

    /**
     * 建構函式（包含錯誤碼）
     * 
     * @param message   例外訊息
     * @param errorCode 錯誤碼
     */
    public EmployeeAlreadyExistsException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * 建構函式（包含原因）
     * 
     * @param message 例外訊息
     * @param cause   原始例外
     */
    public EmployeeAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "EMPLOYEE_ALREADY_EXISTS";
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
