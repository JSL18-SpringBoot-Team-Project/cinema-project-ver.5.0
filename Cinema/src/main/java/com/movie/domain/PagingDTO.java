package com.movie.domain;

import lombok.Data;
import java.util.List;

@Data
public class PagingDTO<T> {
    private List<T> content;     // 현재 페이지 데이터
    private int currentPage;     // 현재 페이지 번호
    private int totalPages;      // 총 페이지 수
    private long totalItems;     // 총 항목 수
    private int pageSize;        // 한 페이지당 항목 수

    public PagingDTO(List<T> content, int currentPage, int totalPages, long totalItems, int pageSize) {
        this.content = content;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.pageSize = pageSize;
    }
}
