package ru.ifmo.web.service.exception;

import lombok.Getter;

import javax.xml.ws.WebFault;

@WebFault(faultBean = "ru.ifmo.web.service.exception.MetallicaServiceFault")
public class MetallicaServiceException extends Exception {

  @Getter
  private final String faultInfo;

  public MetallicaServiceException( String faultInfo ) {
    this.faultInfo = faultInfo;
  }
}
