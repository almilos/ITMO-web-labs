package ru.ifmo.web.deploy;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.ifmo.web.database.dao.MetallicaDAO;
import ru.ifmo.web.database.entity.Metallica;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@WebService( serviceName = "metallica",targetNamespace = "metallica_namespace" )
@AllArgsConstructor
@NoArgsConstructor
public class MetallicaService {
  @Inject
  private MetallicaDAO metallicaDAO;

  @WebMethod
  public List<Metallica> findAll( ) throws SQLException {
    return metallicaDAO.findAll( );
  }

  @WebMethod
  public List<Metallica> findWithFilters(
    @WebParam( name = "id" ) Long id, 
    @WebParam( name = "name" ) String name,          
    @WebParam( name = "instrument" ) String instrument,
    @WebParam( name = "date_entry" ) Date date_entry,
    @WebParam( name = "net_worth" ) Integer net_worth, 
    @WebParam( name = "birthdate" ) Date birthdate
  ) throws SQLException {
    return metallicaDAO.findWithFilters( id, name, instrument, date_entry, net_worth, birthdate );
  }

}
