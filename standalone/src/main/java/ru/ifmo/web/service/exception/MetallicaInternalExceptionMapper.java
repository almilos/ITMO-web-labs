package ru.ifmo.web.service.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class MetallicaInternalExceptionMapper implements ExceptionMapper<MetallicaInternalException> {
  @Override
  public Response toResponse(MetallicaInternalException e) {
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getReason()).build();
  }
}