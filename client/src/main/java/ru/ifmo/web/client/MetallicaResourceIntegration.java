package ru.ifmo.web.client;

import lombok.extern.slf4j.Slf4j;
import ru.ifmo.web.database.entity.Metallica;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import java.util.List;
import javax.ws.rs.core.MediaType;
import java.util.Date;

import java.text.SimpleDateFormat;

@Slf4j
public class MetallicaResourceIntegration {
  private final String findAllUrl = "http://localhost:8080/metallica/all";
  private final String filterUrl = "http://localhost:8080/metallica/filter";

  private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

  public List<Metallica> findAll() {
    Client client = Client.create();
    WebResource webResource = client.resource(findAllUrl);
    ClientResponse response =
        webResource.accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
    if (response.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
      throw new IllegalStateException("Request failed");
    }
    GenericType<List<Metallica>> type = new GenericType<List<Metallica>>() {
    };
    return response.getEntity(type);
  }

  public List<Metallica> findWithFilters( Long id, String name, String instrument, Date entrydate, Integer networth, Date birthdate ) {
    Client client = Client.create();
    WebResource webResource = client.resource(filterUrl);
    if (id != null) {
      webResource = webResource.queryParam("id", id + "");
    }

    if (name != null) {
      webResource = webResource.queryParam("name", name);
    }

    if (instrument != null) {
      webResource = webResource.queryParam("instrument", instrument);
    }

    if (entrydate != null) {
      webResource = webResource.queryParam("entrydate", sdf.format(entrydate));
    }

    if (networth != null) {
      webResource = webResource.queryParam("networth", networth + "");
    }

    if (birthdate != null) {
      webResource = webResource.queryParam("birthdate", sdf.format(birthdate));
    }

    ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
    if (response.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
      log.debug( response.toString( ) );
      throw new IllegalStateException("Request failed");
    }
    log.debug( response.toString( ) );
    GenericType<List<Metallica>> type = new GenericType<List<Metallica>>() {
    };
    return response.getEntity(type);
  }
}