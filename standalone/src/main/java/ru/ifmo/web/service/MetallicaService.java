package ru.ifmo.web.service;

import ru.ifmo.web.database.dao.MetallicaDAO;
import ru.ifmo.web.database.entity.Metallica;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.Map;

import ru.ifmo.web.service.exception.MetallicaServiceException;
import ru.ifmo.web.service.exception.MetallicaLoginException;

@WebService( serviceName = "metallica", targetNamespace = "metallica_namespace" )
public class MetallicaService {
  private MetallicaDAO metallicaDAO;

  public MetallicaService( MetallicaDAO metallicaDAO ) {
    this.metallicaDAO = metallicaDAO;
  }

  public MetallicaService() {}

  @Resource
  private WebServiceContext wsctx;

  private int authenticate( ) throws MetallicaLoginException {
    MessageContext mctx = wsctx.getMessageContext();
		
    Map http_headers = (Map) mctx.get(MessageContext.HTTP_REQUEST_HEADERS);
    List userList = (List) http_headers.get("username");
    List passList = (List) http_headers.get("password");

    String username = "";
    String password = "";
        
    if(userList!=null) {
      username = userList.get(0).toString();
    }
        	
    if(passList!=null) {
      password = passList.get(0).toString();
    }
          
    if (username.equals("user") && password.equals("pass")){
      return 0;
    } else {
      throw new MetallicaLoginException( );
    }
  }

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
      authenticate( );
    } catch( MetallicaLoginException e ) {
      throw new MetallicaServiceException( "Permission denied" );
    }

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
      authenticate( );
    } catch( MetallicaLoginException e ) {
      throw new MetallicaServiceException( "Permission denied" );
    }

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
      authenticate( );
    } catch( MetallicaLoginException e ) {
      throw new MetallicaServiceException( "Permission denied" );
    }

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
