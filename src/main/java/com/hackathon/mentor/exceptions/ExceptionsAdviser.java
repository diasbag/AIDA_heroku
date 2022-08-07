package com.hackathon.mentor.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionsAdviser {
    @ResponseBody
    @ExceptionHandler(AccountNotFound.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    String accountNotFound(AccountNotFound ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(AccountBadRequest.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    String badRequest(AccountBadRequest ex) {
        return ex.getMessage();
    }


    @ResponseBody
    @ExceptionHandler(AuthenticationFailed.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    String authenticationException(AuthenticationFailed ex) {
        return ex.getMessage();
    }


    @ResponseBody
    @ExceptionHandler(AccountConflict.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    String accountConflict(AccountConflict ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(EmitterGone.class)
    @ResponseStatus(value = HttpStatus.GONE)
    String emitterGone(EmitterGone ex) {
        return ex.getMessage();
    }


}
