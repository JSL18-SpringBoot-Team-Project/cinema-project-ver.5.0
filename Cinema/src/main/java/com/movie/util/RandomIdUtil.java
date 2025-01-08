package com.movie.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class RandomIdUtil {

    public String makePaymentId() {

        String uniqueFilename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        return uniqueFilename;
    }

}
