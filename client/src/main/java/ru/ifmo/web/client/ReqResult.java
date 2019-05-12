package ru.ifmo.web.client;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReqResult<T> {
    private boolean err = false;
    private String errorMessage;
    private T result;
}