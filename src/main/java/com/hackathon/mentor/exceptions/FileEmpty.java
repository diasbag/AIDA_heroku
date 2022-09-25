package com.hackathon.mentor.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileEmpty extends RuntimeException{
    public FileEmpty(String input) {
        super("File is empty: " + input);
        log.info("File is empty: " + input);
    }
}