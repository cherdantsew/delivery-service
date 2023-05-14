package com.orderengine.deliver.deliverservice.exception.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
    code = HttpStatus.FORBIDDEN,
    reason = "FORBIDDEN"
)
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
