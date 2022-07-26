package com.hackathon.mentor.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerFail extends RuntimeException{
    public ServerFail(String input) {
        super("Server fail: " + input);
        log.info("Server fail: " + input);
    }
}