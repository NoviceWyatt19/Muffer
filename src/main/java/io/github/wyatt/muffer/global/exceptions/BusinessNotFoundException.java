package io.github.wyatt.muffer.global.exceptions;

import io.github.wyatt.muffer.global.config.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
