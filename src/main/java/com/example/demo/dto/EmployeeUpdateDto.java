package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Employee 更新請求 DTO
 * <p>
 * 用於接收更新員工資訊的 API 請求資料，包含必要的驗證註解。
 * 與建立 DTO 不同的是，此 DTO 包含員工 ID 用於識別要更新的員工。
 * </p>
 * 
 * @author System Generated
 * @version 1.0
 * @since 2025-01-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateDto {

    /**
     * 員工 ID
     */
    @JsonProperty("emp_id")
    @NotNull(message = "員工 ID 不能為空")
    @Positive(message = "員工 ID 必須為正整數")
    private Integer empId;

    /**
     * 員工編號
     */
    @JsonProperty("emp_code")
    @Size(max = 20, message = "員工編號長度不能超過20個字元")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "員工編號只能包含大寫字母和數字")
    private String empCode;

    /**
     * 員工姓名
     */
    @JsonProperty("emp_name")
    @Size(min = 2, max = 50, message = "員工姓名長度必須在2-50個字元之間")
    private String empName;

    /**
     * 員工電子郵箱
     */
    @JsonProperty("emp_email")
    @Email(message = "請輸入有效的電子郵箱格式")
    @Size(max = 100, message = "電子郵箱長度不能超過100個字元")
    private String empEmail;

    /**
     * 員工連絡電話
     */
    @JsonProperty("emp_phone")
    @Size(max = 20, message = "連絡電話長度不能超過20個字元")
    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "請輸入有效的電話號碼格式")
    private String empPhone;

    /**
     * 部門 ID
     */
    @JsonProperty("dept_id")
    @Positive(message = "部門 ID 必須為正整數")
    private Integer deptId;

    /**
     * 職位名稱
     */
    @JsonProperty("job_title")
    @Size(max = 100, message = "職位名稱長度不能超過100個字元")
    private String jobTitle;

    /**
     * 入職日期
     */
    @JsonProperty("hire_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "入職日期不能是未來日期")
    private LocalDate hireDate;

    /**
     * 離職日期
     */
    @JsonProperty("resign_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate resignDate;

    /**
     * 員工狀態
     */
    @JsonProperty("is_active")
    @NotNull(message = "員工狀態不能為空")
    private Boolean isActive;

    /**
     * 更新者
     */
    @JsonProperty("update_user")
    @NotBlank(message = "更新者不能為空")
    @Size(max = 20, message = "更新者名稱長度不能超過20個字元")
    private String updateUser;
}
