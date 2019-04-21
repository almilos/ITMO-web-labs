package ru.ifmo.web.deploy;

import ru.ifmo.web.database.dao.MetallicaDAO;
import ru.ifmo.web.database.entity.Metallica;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jws.WebMethod;
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

@RequestScoped
@Path("/metallica")
@Produces({MediaType.APPLICATION_JSON})
public class MetallicaService {
  @Inject
  private MetallicaDAO metallicaDAO;
  private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

  @WebMethod
  @GET
  @Path("/all")
  public List<Metallica> findAll( ) throws SQLException {
    return metallicaDAO.findAll( );
  }

  @WebMethod
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

  @WebMethod
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

  @WebMethod
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

  @WebMethod
  @DELETE
  @Path("/delete")
  public String delete(
    @QueryParam( "id" ) Long id
  ) throws SQLException {
    return metallicaDAO.delete( id ) + "";
  }
}
