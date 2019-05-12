package ru.ifmo.web.service.exception;

import lombok.Getter;

public class MetallicaIdNotFoundException extends RuntimeException {
    @Getter
    private final String reason;

    public MetallicaIdNotFoundException(String message) {
        this.reason = message;
    }
}