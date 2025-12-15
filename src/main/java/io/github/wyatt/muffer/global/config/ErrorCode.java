package io.github.wyatt.muffer.global.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    FORBIDDEN_MODIFY("4031", HttpStatus.FORBIDDEN, "허용되지 않는 수정 요청입니다."),
    OTHER_USER("4032", HttpStatus.FORBIDDEN, "해당 리소스에 대한 권한이 없습니다.");


    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(String errorCode, HttpStatus httpStatus, String message) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
