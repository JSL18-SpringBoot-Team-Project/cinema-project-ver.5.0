package com.movie.mapper;

import com.movie.domain.Inquiries;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface InquiryMapper {
    // 문의 등록
    void insertInquiry(Inquiries inquiries);

    // 문의 목록 조회 (필터와 페이징 포함)
    List<Inquiries> selectInquiries(Map<String, Object> params);

    // 필터 적용된 문의 수 조회
    long countFilteredInquiries(Map<String, Object> params);

    // 답변 삽입
    void insertAnswer(Map<String, Object> params);

    // 문의 상태 업데이트
    void updateInquiryStatus(Map<String, Object> params);

    // 특정 사용자 문의내역 조회
    List<Inquiries> getInquiriesByUserId(@Param("userId") long userId);

    // 특정 문의 상세 조회
    @Select("SELECT * FROM inquiries WHERE id = #{inquiryId}")
    Inquiries getInquiryById(@Param("inquiryId") long inquiryId);

    // 답변 삽입 결과 반환
    int insertAnswerAndReturnId(@Param("inquiryId") long inquiryId, @Param("content") String content);
}

