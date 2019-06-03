package ru.ifmo.web.client;

import lombok.extern.slf4j.Slf4j;
import ru.ifmo.web.database.entity.Metallica;
import ru.ifmo.web.client.ReqResult;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import java.util.List;
import javax.ws.rs.core.MediaType;
import java.util.Date;

import java.text.SimpleDateFormat;

@Slf4j
public class MetallicaResourceIntegration {
  private final String findAllUrl = "http://localhost:8080/metallica/all";
  private final String filterUrl = "http://localhost:8080/metallica/filter";
  private final String createUrl = "http://localhost:8080/metallica/create";
  private final String updateUrl = "http://localhost:8080/metallica/update";
  private final String deleteUrl = "http://localhost:8080/metallica/delete";

  private final String user = "user";
  private final String pass = "pass";

  private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

  public ReqResult<List<Metallica>> findAll() {
    Client client = Client.create();
    client.addFilter(new HTTPBasicAuthFilter(user, pass));
    WebResource webResource = client.resource(findAllUrl);
    ClientResponse response =
        webResource.accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
    if (response.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
      GenericType<String> type = new GenericType<String>() {};
      return new ReqResult<>(true, response.getEntity(type), null);
    }
    GenericType<List<Metallica>> type = new GenericType<List<Metallica>>() {
    };
    return new ReqResult<>(false, null, response.getEntity(type));
  }

  public ReqResult<List<Metallica>> findWithFilters( Long id, String name, String instrument, Date entrydate, Integer networth, Date birthdate ) {
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
      GenericType<String> type = new GenericType<String>() {};
      return new ReqResult<>(true, response.getEntity(type), null);
    }
    GenericType<List<Metallica>> type = new GenericType<List<Metallica>>() {
    };
    return new ReqResult<>(false, null, response.getEntity(type));
  }

  public ReqResult<Long> create( String name, String instrument, Date entrydate, Integer networth, Date birthdate ) {
    Client client = Client.create();
    WebResource webResource = client.resource(createUrl);

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

    ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class);
    if (response.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
      GenericType<String> type = new GenericType<String>() {};
      return new ReqResult<>(true, response.getEntity(type), null);
    }
    GenericType<String> type = new GenericType<String>() {
    };
    //return Long.parseLong( response.getEntity(type) );
    return new ReqResult<>(false, null, Long.parseLong(response.getEntity(type)));
  }

  public ReqResult<Integer> update( Long id, String name, String instrument, Date entrydate, Integer networth, Date birthdate ) {
    Client client = Client.create();
    WebResource webResource = client.resource(updateUrl);

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

    ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON_TYPE).put(ClientResponse.class);
    if (response.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
      GenericType<String> type = new GenericType<String>() {};
      return new ReqResult<>(true, response.getEntity(type), null);
    }
    GenericType<String> type = new GenericType<String>() {
    };
    return new ReqResult<>(false, null, Integer.parseInt(response.getEntity(type)));
  }

  public ReqResult<Integer> delete( Long id ) {
    Client client = Client.create();
    WebResource webResource = client.resource(deleteUrl);

    if (id != null) {
      webResource = webResource.queryParam("id", id + "");
    }

    ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON_TYPE).delete(ClientResponse.class);
    if (response.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
      GenericType<String> type = new GenericType<String>() {};
      return new ReqResult<>(true, response.getEntity(type), null);
    }
    GenericType<String> type = new GenericType<String>() {
    };
    return new ReqResult<>(false, null, Integer.parseInt(response.getEntity(type)));
  }
}