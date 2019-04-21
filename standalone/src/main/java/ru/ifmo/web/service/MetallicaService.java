package ru.ifmo.web.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import ru.ifmo.web.standalone.App;
import ru.ifmo.web.database.dao.MetallicaDAO;
import ru.ifmo.web.database.entity.Metallica;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import java.sql.Connection;
import java.sql.DriverManager;

@Data
@Slf4j
@Path("/metallica")
@Produces({MediaType.APPLICATION_JSON})
public class MetallicaService {
  private MetallicaDAO metallicaDAO;
  private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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
    @QueryParam( "entrydate" ) String entrydate,
    @QueryParam( "networth" ) Integer networth, 
    @QueryParam( "birthdate" ) String birthdate
  ) throws SQLException, ParseException {
    Date bd = null;
    Date ed = null;
    try {
      if( birthdate != null ) bd = sdf.parse(birthdate);
    } catch (ParseException e) {
      bd = null;
    }
    try {
      if( entrydate != null ) ed = sdf.parse(entrydate);
    } catch (ParseException e) {
      ed = null;
    }
    return metallicaDAO.findWithFilters( id, name, instrument, ed, networth, bd );
  }

  @POST
  @Path("/create")
  public String create(
    @QueryParam( "name" ) String name,                  
    @QueryParam( "instrument" ) String instrument,
    @QueryParam( "entrydate" ) String entrydate,
    @QueryParam( "networth" ) Integer networth, 
    @QueryParam( "birthdate" ) String birthdate
  ) throws SQLException, ParseException {
    Date bd = null;
    Date ed = null;
    try {
      if( birthdate != null ) bd = sdf.parse(birthdate);
    } catch (ParseException e) {
      bd = null;
    }
    try {
      if( entrydate != null ) ed = sdf.parse(entrydate);
    } catch (ParseException e) {
      ed = null;
    }
    return metallicaDAO.create( name, instrument, ed, networth, bd ) + "";
  }

  @PUT
  @Path("/update")
  public String update(
    @QueryParam( "id" ) Long id, 
    @QueryParam( "name" ) String name,                  
    @QueryParam( "instrument" ) String instrument,
    @QueryParam( "entrydate" ) String entrydate,
    @QueryParam( "networth" ) Integer networth, 
    @QueryParam( "birthdate" ) String birthdate
  ) throws SQLException, ParseException {
    Date bd = null;
    Date ed = null;
    try {
      if( birthdate != null ) bd = sdf.parse(birthdate);
    } catch (ParseException e) {
      bd = null;
    }
    try {
      if( entrydate != null ) ed = sdf.parse(entrydate);
    } catch (ParseException e) {
      ed = null;
    }
    return metallicaDAO.update( id, name, instrument, ed, networth, bd ) + "";
  }

  @DELETE
  @Path("/delete")
  public String delete(
    @QueryParam( "id" ) Long id
  ) throws SQLException {
    return metallicaDAO.delete( id ) + "";
  }
}
