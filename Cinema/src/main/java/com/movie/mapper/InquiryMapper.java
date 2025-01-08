package com.movie.mapper;

import com.movie.domain.Inquiries;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface InquiryMapper {
    void insertInquiry(Inquiries inquiries);

    List<Inquiries> selectInquiries(Map<String, Object> params);

    long countFilteredInquiries(Map<String, Object> params);

    void insertAnswer(Map<String, Object> params);

    void updateInquiryStatus(Map<String, Object> params);
}

