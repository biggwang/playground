package com.biggwang.springconfigclient;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(RequestNotPermitted.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ErrorResponse handleRequestNotPermitted() {
        log.error("TOO_MANY_REQUESTS");
        return new ErrorResponse();
    }
}

@AllArgsConstructor
class ErrorResponse {
    private final String message = "요청이 너무 많아 거부 되었습니다.";


}