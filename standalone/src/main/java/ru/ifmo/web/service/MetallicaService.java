package ru.ifmo.web.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import ru.ifmo.web.standalone.App;
import ru.ifmo.web.database.dao.MetallicaDAO;
import ru.ifmo.web.database.entity.Metallica;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;

@Data
@Slf4j
@Path("/metallica")
@Produces({MediaType.APPLICATION_JSON})
public class MetallicaService {
  private MetallicaDAO metallicaDAO;

  public MetallicaService() throws SQLException {
    Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/metallica_db", "webuser", "password");
    this.metallicaDAO = new MetallicaDAO(connection);
  }

  @GET
  @Path("/all")
  public List<Metallica> findAll( ) throws SQLException {
    return metallicaDAO.findAll( );
  }

  @GET
  @Path("/filter")
  public List<Metallica> findWithFilters(
    @QueryParam( "id" ) Long id, 
    @QueryParam( "name" ) String name,                  
    @QueryParam( "instrument" ) String instrument,
    @QueryParam( "entrydate" ) Date entrydate,
    @QueryParam( "networth" ) Integer networth, 
    @QueryParam( "birthdate" ) Date birthdate
  ) throws SQLException {
    return metallicaDAO.findWithFilters( id, name, instrument, entrydate, networth, birthdate );
  }

}
