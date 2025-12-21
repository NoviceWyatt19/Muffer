package io.github.wyatt.muffer.global.exceptions;

import io.github.wyatt.muffer.global.config.ErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class UnauthenticationException extends AuthenticationException {

  private final ErrorCode errorCode;

  public UnauthenticationException(ErrorCode errorCode) {
    super(errorCode.getMessage());
      this.errorCode = errorCode;
  }

}
