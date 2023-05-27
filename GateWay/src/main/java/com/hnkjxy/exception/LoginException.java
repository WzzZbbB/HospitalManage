package com.hnkjxy.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-29 17:43
 */
public class LoginException extends AuthenticationException {
    public LoginException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public LoginException(String msg) {
        super(msg);
    }
}
