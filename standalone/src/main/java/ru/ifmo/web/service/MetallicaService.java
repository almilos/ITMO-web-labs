package ru.ifmo.web.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.ifmo.web.database.dao.MetallicaDAO;
import ru.ifmo.web.database.entity.Metallica;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@WebService( serviceName = "metallica", targetNamespace = "metallica_namespace" )
@AllArgsConstructor
@NoArgsConstructor
public class MetallicaService {
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
    @WebParam( name = "entrydate" ) Date entrydate,
    @WebParam( name = "networth" ) Integer networth, 
    @WebParam( name = "birthdate" ) Date birthdate
  ) throws SQLException {
    return metallicaDAO.findWithFilters( id, name, instrument, entrydate, networth, birthdate );
  }

}
