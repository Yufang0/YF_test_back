package com.example.demo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 分頁回應資料格式
 * <p>
 * 用於封裝分頁查詢結果的標準格式，包含當前頁資料、分頁資訊等。
 * 提供從 Spring Data 的 Page 物件轉換的靜態方法。
 * </p>
 * 
 * @param <T> 分頁資料的類型
 * @author System Generated
 * @version 1.0
 * @since 2025-01-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    /**
     * 當前頁資料
     */
    @JsonProperty("content")
    private List<T> content;

    /**
     * 當前頁碼（0-based）
     */
    @JsonProperty("page")
    private Integer page;

    /**
     * 每頁大小
     */
    @JsonProperty("size")
    private Integer size;

    /**
     * 總記錄數
     */
    @JsonProperty("total_elements")
    private Long totalElements;

    /**
     * 總頁數
     */
    @JsonProperty("total_pages")
    private Integer totalPages;

    /**
     * 是否有下一頁
     */
    @JsonProperty("has_next")
    private Boolean hasNext;

    /**
     * 是否有上一頁
     */
    @JsonProperty("has_previous")
    private Boolean hasPrevious;

    /**
     * 是否為第一頁
     */
    @JsonProperty("is_first")
    private Boolean isFirst;

    /**
     * 是否為最後一頁
     */
    @JsonProperty("is_last")
    private Boolean isLast;

    /**
     * 從 Spring Data Page 物件建立分頁回應
     * 
     * @param page Spring Data Page 物件
     * @param <T>  資料類型
     * @return 分頁回應物件
     */
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious(),
                page.isFirst(),
                page.isLast());
    }
}
