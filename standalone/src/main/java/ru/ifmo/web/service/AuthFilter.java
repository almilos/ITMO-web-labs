package ru.ifmo.web.service;

import lombok.extern.slf4j.Slf4j;
import ru.ifmo.web.database.entity.Metallica;

import javax.inject.Inject;
import com.sun.jersey.spi.container.ContainerRequest;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.container.PreMatching;
import javax.xml.bind.DatatypeConverter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.Priorities;
import javax.annotation.Priority;
 
@Slf4j
@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION) 
public class AuthFilter implements ContainerRequestFilter {

  @Override
  public void filter(ContainerRequestContext containerRequest) throws WebApplicationException {
    String method = containerRequest.getMethod();
    String path = containerRequest.getUriInfo().getPath(true);
 
    String auth = containerRequest.getHeaderString("authorization");
 
    if(auth == null){
      throw new WebApplicationException(Status.UNAUTHORIZED);
    }
 
    auth = auth.replaceFirst("[B|b]asic ", "");
 
    byte[] decodedBytes = DatatypeConverter.parseBase64Binary(auth);
 
    String[] lap = new String(decodedBytes).split(":", 2);
 
    if(lap == null || lap.length != 2){
      throw new WebApplicationException(Status.UNAUTHORIZED);
    }
 
    if(lap[0] == "user" && lap[1] == "pass" ){
      return;
    } else {
      throw new WebApplicationException(Status.UNAUTHORIZED);
    }
  }
}
