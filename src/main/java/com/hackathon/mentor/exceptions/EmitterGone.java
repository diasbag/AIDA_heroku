package com.hackathon.mentor.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmitterGone extends RuntimeException{
    public EmitterGone(String input) {
        super("Gone: " + input);
        log.info("Gone: " + input);
    }
}