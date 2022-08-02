package com.hackathon.mentor.models;

import lombok.NoArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.Serializable;
@NoArgsConstructor
public class SerializableSSE extends SseEmitter implements Serializable {

    public SerializableSSE(Long timeout) {
        super(timeout);
    }
}
