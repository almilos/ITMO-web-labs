package ru.ifmo.web.deploy;

import ru.ifmo.web.database.dao.MetallicaDAO;
import ru.ifmo.web.database.entity.Metallica;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@RequestScoped
@Path("/metallica")
@Produces({MediaType.APPLICATION_JSON})
public class MetallicaService {
  @Inject
  private MetallicaDAO metallicaDAO;

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
}
