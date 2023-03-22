package dev.app.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthenticateException extends AuthenticationException {
    public AuthenticateException(String msg, Throwable t) {
        super(msg, t);
    }

    public AuthenticateException(String msg) {
        super(msg);
    }
}
