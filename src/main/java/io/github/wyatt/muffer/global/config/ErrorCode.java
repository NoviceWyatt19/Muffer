package io.github.wyatt.muffer.global.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    FORBIDDEN_MODIFY("4031", HttpStatus.FORBIDDEN, "허용되지 않는 수정 요청입니다."),
    OTHER_USER("4032", HttpStatus.FORBIDDEN, "해당 리소스에 대한 권한이 없습니다."),
    NOT_FOUND_DATA("4041", HttpStatus.NOT_FOUND, "해당 데이터를 찾지 못했습니다."),
    INVALID_REQUEST("4011", HttpStatus.UNAUTHORIZED, "잘못된 로그인 요청입니다."),
    INVALID_CREDENTIALS("4012", HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."),
    AUTHENTICATION_FAILED("4013", HttpStatus.UNAUTHORIZED, "인증에 실패했습니다.");


    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(String errorCode, HttpStatus httpStatus, String message) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
