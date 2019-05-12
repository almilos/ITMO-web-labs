package ru.ifmo.web.service.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class MetallicaIdNotFoundExceptionMapper implements ExceptionMapper<MetallicaIdNotFoundException> {
    @Override
    public Response toResponse(MetallicaIdNotFoundException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(e.getReason()).build();
    }
}