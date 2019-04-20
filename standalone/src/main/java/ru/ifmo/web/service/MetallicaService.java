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

import ru.ifmo.web.service.exception.MetallicaServiceException;

@WebService( serviceName = "metallica", targetNamespace = "metallica_namespace" )
@AllArgsConstructor
@NoArgsConstructor
public class MetallicaService {
  private MetallicaDAO metallicaDAO;

  @WebMethod
  public List<Metallica> findAll( ) throws MetallicaServiceException {
    try {
      return metallicaDAO.findAll( );
    } catch( SQLException e ) {
      throw new MetallicaServiceException( e.getMessage( ) );
    }
  }

  @WebMethod
  public Long create(
    @WebParam( name = "name" ) String name,          
    @WebParam( name = "instrument" ) String instrument,
    @WebParam( name = "entrydate" ) Date entrydate,
    @WebParam( name = "networth" ) Integer networth, 
    @WebParam( name = "birthdate" ) Date birthdate
  ) throws MetallicaServiceException {
    try {
      return metallicaDAO.create( name, instrument, entrydate, networth, birthdate );
    } catch( SQLException e ) {
      throw new MetallicaServiceException( e.getMessage( ) );
    }
  }

  @WebMethod
  public int update(
    @WebParam( name = "id" ) Long id, 
    @WebParam( name = "name" ) String name,          
    @WebParam( name = "instrument" ) String instrument,
    @WebParam( name = "entrydate" ) Date entrydate,
    @WebParam( name = "networth" ) Integer networth, 
    @WebParam( name = "birthdate" ) Date birthdate
  ) throws MetallicaServiceException {
    try {
      int ret = metallicaDAO.update( id, name, instrument, entrydate, networth, birthdate );
      if( ret == 0 )
        throw new MetallicaServiceException( "No member with given ID" );
      return ret;
    } catch( SQLException e ) {
      throw new MetallicaServiceException( e.getMessage( ) );
    }
  }

  @WebMethod
  public int delete(
    @WebParam( name = "id" ) Long id
  ) throws MetallicaServiceException {
    try {
      int ret = metallicaDAO.delete( id );
      if( ret == 0 )
        throw new MetallicaServiceException( "No member with given ID" );
      return ret;
    } catch( SQLException e ) {
      throw new MetallicaServiceException( e.getMessage( ) );
    }
  }

  @WebMethod
  public List<Metallica> findWithFilters(
    @WebParam( name = "id" ) Long id, 
    @WebParam( name = "name" ) String name,                  
    @WebParam( name = "instrument" ) String instrument,
    @WebParam( name = "entrydate" ) Date entrydate,
    @WebParam( name = "networth" ) Integer networth, 
    @WebParam( name = "birthdate" ) Date birthdate
  ) throws MetallicaServiceException {
    try {
      return metallicaDAO.findWithFilters( id, name, instrument, entrydate, networth, birthdate );
    } catch( SQLException e ) {
      throw new MetallicaServiceException( e.getMessage( ) );
    }
  }

}
