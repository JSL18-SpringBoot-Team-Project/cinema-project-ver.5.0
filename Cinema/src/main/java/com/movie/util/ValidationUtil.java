package com.movie.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ValidationUtil {
    //	이메일 정규 표현식
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    //	인증번호 정규 표현식
    private static final String CODE_REGEX = "^[0-9]{6}$";

    //	비밀번호 정규 표현식 (8~16자의 영문, 숫자, 특수문자 조합)
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,16}$";

    //	생년월일 정규 표현식
    private static final String BIRTHDATE_REGEX = "^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";

    //	전화번호 정규 표현식
    private static final String PHONE_REGEX = "^01[016789]-?\\d{3,4}-?\\d{4}$";

    private boolean validate(String input, String regex) {
        return input != null && Pattern.matches(regex, input);
    }

    //	이메일 형식 검증
    public boolean isValidEmail(String email) {
        return validate(email, EMAIL_REGEX);
    }

    //	인증번호 정규 표현식
    public boolean isValidCode(String code) {
        return validate(code, CODE_REGEX);
    }

    //	비밀번호 유효성 검증
    public boolean isValidPassword(String password) {
        return validate(password, PASSWORD_REGEX);
    }

    //	생년월일 형식 검증
    public boolean isValidBirthdate(String birthdate) {
        return validate(birthdate, BIRTHDATE_REGEX);
    }

    //	전화번호 형식 검증
    public boolean isValidPhoneNumber(String phoneNumber) {
        return validate(phoneNumber, PHONE_REGEX);
    }
}
