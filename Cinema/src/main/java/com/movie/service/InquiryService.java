package com.movie.service;

import com.movie.domain.Inquiries;
import com.movie.domain.PagingDTO;
import com.movie.mapper.InquiryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InquiryService {
    @Autowired
    private InquiryMapper inquiryMapper;

    @Autowired
    private PagingService pagingService;

    public void insertInquiry(Inquiries inquiry) {
        inquiryMapper.insertInquiry(inquiry);
    }

    public PagingDTO<Inquiries> getPagedInquiries(String inquiryType, int page, int pageSize) {
        int offset = (page - 1) * pageSize;

//      매개변수 매핑
        Map<String, Object> params = new HashMap<>();
        params.put("inquiryType", inquiryType);
        params.put("pageSize", pageSize);
        params.put("offset", offset);

        List<Inquiries> inquiries = inquiryMapper.selectInquiries(params);

        inquiries.forEach(inquiry -> System.out.println("문의날짜: " + inquiry.getCreated_at()));

        long totalCount = inquiryMapper.countFilteredInquiries(params);
        return pagingService.createPaging(inquiries, totalCount, page, pageSize);
    }

    public void addAnswer(int inquiryId, String content) {
        // 답변 등록
        Map<String, Object> params = Map.of("inquiryId", inquiryId, "content", content);
        inquiryMapper.insertAnswer(params);

        // 문의 상태 업데이트
        inquiryMapper.updateInquiryStatus(Map.of("inquiryId", inquiryId, "status", "ANSWERED"));
    }
}
