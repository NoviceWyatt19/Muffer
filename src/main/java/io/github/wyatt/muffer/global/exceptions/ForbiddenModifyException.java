package io.github.wyatt.muffer.global.exceptions;

import io.github.wyatt.muffer.global.config.ErrorCode;
import lombok.Getter;

@Getter
public class ForbiddenModifyException extends RuntimeException {
    private final ErrorCode errorCode;

    public ForbiddenModifyException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
