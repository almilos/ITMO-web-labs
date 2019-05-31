package ru.ifmo.web.service.exception;

import lombok.Getter;

import javax.xml.ws.WebFault;

@WebFault(faultBean = "ru.ifmo.web.service.exception.MetallicaLoginFault")
public class MetallicaLoginException extends Exception {
  public MetallicaLoginException(  ) {  }
}

