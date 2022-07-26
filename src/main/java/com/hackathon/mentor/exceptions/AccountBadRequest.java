package com.hackathon.mentor.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountBadRequest extends RuntimeException{
    public AccountBadRequest(String input) {
        super("Wrong input: " + input);
        log.info("Wrong input: " + input);
    }
}