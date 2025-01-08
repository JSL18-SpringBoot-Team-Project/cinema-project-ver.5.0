package com.movie.service;

import com.movie.domain.PagingDTO;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PagingService {

    public <T> PagingDTO<T> createPaging(List<T> items, long totalCount, int page, int pageSize) {
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        return new PagingDTO<>(
                items,
                page,
                totalPages,
                totalCount,
                pageSize
        );
    }
}