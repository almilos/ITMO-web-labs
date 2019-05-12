package ru.ifmo.web.service.exception;

import lombok.Getter;

public class MetallicaInternalException extends RuntimeException {
  @Getter
  private final String reason;

  public MetallicaInternalException(String reason) {
    this.reason = reason;
  }
}