package com.movie.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Data
@Alias("Inquiries")
public class Inquiries {
    private Integer id;
    private Integer user_id;
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;
    private InquiryType inquiry_type;
    @NotEmpty(message = "Content cannot be empty")
    private String content;
    private Status status;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public enum InquiryType {
        RESERVATION,
        PAYMENT,
        ACCOUNT,
        MOVIE_INFO,
        SYSTEM_ERROR,
        COUPONS_EVENTS,
        OTHERS
    }

    public enum Status {
        PENDING,
        ANSWERED
    }
}
