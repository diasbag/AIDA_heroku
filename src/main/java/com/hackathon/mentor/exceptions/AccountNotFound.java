package com.hackathon.mentor.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountNotFound extends RuntimeException{
    public AccountNotFound(String id) {
        super("Not found: " + id);
        log.info("Not found: " + id);
    }
}
