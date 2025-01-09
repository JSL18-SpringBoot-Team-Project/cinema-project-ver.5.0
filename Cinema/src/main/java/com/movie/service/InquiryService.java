package com.movie.service;

import com.movie.domain.Inquiries;
import com.movie.domain.PagingDTO;
import com.movie.mapper.InquiryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InquiryService {
    private final InquiryMapper inquiryMapper;
    private final PagingService pagingService;

    @Autowired
    public InquiryService(InquiryMapper inquiryMapper, PagingService pagingService) {
        this.inquiryMapper = inquiryMapper;
        this.pagingService = pagingService;
    }

    /**
     * 문의 등록
     * @param inquiry 등록할 문의 객체
     */
    @Transactional
    public void insertInquiry(Inquiries inquiry) {
        inquiryMapper.insertInquiry(inquiry);
    }

    /**
     * 페이징 및 필터가 적용된 문의 목록 조회
     * @param inquiryType 문의 유형 필터
     * @param page 현재 페이지 번호
     * @param pageSize 한 페이지의 데이터 수
     * @return 페이징된 문의 목록
     */
    public PagingDTO<Inquiries> getPagedInquiries(String inquiryType, int page, int pageSize) {
        int offset = (page - 1) * pageSize;

        // 매개변수 매핑
        Map<String, Object> params = new HashMap<>();
        params.put("inquiryType", inquiryType);
        params.put("pageSize", pageSize);
        params.put("offset", offset);

        // 데이터 조회
        List<Inquiries> inquiries = inquiryMapper.selectInquiries(params);
        long totalCount = inquiryMapper.countFilteredInquiries(params);

        // 페이징 데이터 생성
        return pagingService.createPaging(inquiries, totalCount, page, pageSize);
    }

    /**
     * 문의에 대한 답변 추가 및 상태 업데이트
     * @param inquiryId 문의 ID
     * @param content 답변 내용
     */
    @Transactional
    public void addAnswer(int inquiryId, String content) {
        // 답변 등록
        inquiryMapper.insertAnswer(Map.of(
                "inquiryId", inquiryId,
                "content", content
        ));

        // 문의 상태 업데이트
        inquiryMapper.updateInquiryStatus(Map.of(
                "inquiryId", inquiryId,
                "status", "ANSWERED"
        ));
    }

    /**
     * 특정 사용자 ID로 문의 내역 조회
     *
     * @param userId 사용자 ID
     * @return 사용자의 문의 내역 목록
     */
    public List<Inquiries> getInquiriesByUserId(long userId) {
        return inquiryMapper.getInquiriesByUserId(userId);
    }

    public String getInquiriesNotAnswerCount() {
        String result = "";
        long count = inquiryMapper.getInquiriesNotAnswerCount();
        if(count > 3) {
            result = "3+";
        } else {
            result = String.valueOf(count);
        }
        return result;
    }

    public List<Inquiries> getInquiriesNotAnswer() {
        return inquiryMapper.getInquiriesNotAnswer();
    }
}
