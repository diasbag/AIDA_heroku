package com.hackathon.mentor.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountConflict extends RuntimeException{
    public AccountConflict(String input) {
        super("Conflict: " + input);
        log.info("Conflict: " + input);
    }
}