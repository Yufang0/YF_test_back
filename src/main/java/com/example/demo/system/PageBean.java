package com.example.demo.system;

import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;


@Data
public class PageBean implements Serializable {

    private static final long serialVersionUID = -9045172704261095995L;

	@JsonProperty("total_records")
	private int totalRecords = 0;		// 總筆數
	@JsonProperty("first_index_in_page")
	private int firstIndexInPage = 0;	// 該頁第一筆的 index (由1開始)
	@JsonProperty("last_index_in_page")
	private int lastIndexInPage = 0;	// 該頁最後一筆的 index (由1開始)
	@JsonProperty("pageable")
	private boolean pageable = true;	// 是否要分頁查詢
	@JsonProperty("page_size")
	private int pageSize = 10;		// 每頁筆數
	@JsonProperty("sort_column")
	private String sortColumn;		// 要排序的欄位
	@JsonProperty("sort_direction")
	private String sortDirection;		// 升/降

}
