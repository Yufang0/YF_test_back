package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Employee 建立請求 DTO
 * <p>
 * 用於接收建立員工的 API 請求資料，包含必要的驗證註解以確保資料的完整性和正確性。
 * 此 DTO 用於新建員工時的資料傳輸。
 * </p>
 * 
 * @author System Generated
 * @version 1.0
 * @since 2025-01-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreateDto {

    /**
     * 員工編號，唯一識別碼
     */
    @JsonProperty("emp_code")
    @NotBlank(message = "員工編號不能為空")
    @Size(max = 20, message = "員工編號長度不能超過20個字元")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "員工編號只能包含大寫字母和數字")
    private String empCode;

    /**
     * 員工姓名
     */
    @JsonProperty("emp_name")
    @NotBlank(message = "員工姓名不能為空")
    @Size(min = 2, max = 50, message = "員工姓名長度必須在2-50個字元之間")
    private String empName;

    /**
     * 員工電子郵箱
     */
    @JsonProperty("emp_email")
    @Email(message = "請輸入有效的電子郵箱格式")
    @Size(max = 50, message = "電子郵箱長度不能超過50個字元")
    private String empEmail;

    /**
     * 部門 ID
     */
    @JsonProperty("dept_id")
    @NotNull(message = "部門 ID 不能為空")
    @Positive(message = "部門 ID 必須為正整數")
    private Integer deptId;

    /**
     * 入職日期
     */
    @JsonProperty("hire_date")
    @NotNull(message = "入職日期不能為空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "入職日期不能是未來日期")
    private LocalDate hireDate;

    /**
     * 員工狀態
     */
    @JsonProperty("is_active")
    @NotNull(message = "員工狀態不能為空")
    private Boolean isActive;

    /**
     * 建立者
     */
    @JsonProperty("create_user")
    @Size(max = 20, message = "建立者名稱長度不能超過20個字元")
    private String createUser;
}
