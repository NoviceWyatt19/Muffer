package io.github.wyatt.muffer.global.exceptions;

import dev.paseto.jpaseto.PasetoException;

public class PasetoExpiredException extends PasetoException {
    public PasetoExpiredException(String message) {
        super(message);
    }
}
