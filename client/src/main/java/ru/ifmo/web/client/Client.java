package ru.ifmo.web.client;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

public class Client {
  public static void main( String... args ) throws IOException {
    URL url = new URL( "http://localhost:8080/metallica?wsdl" );
    Metallica_Service metallicaService = new Metallica_Service( url );
    MetallicaService metallicaPort = metallicaService.getMetallicaServicePort( );

    Map<String, Object> requestContext = ((BindingProvider) metallicaPort).getRequestContext();
		Map<String, List<String>> requestHeaders = new HashMap<String, List<String>>();
		requestHeaders.put("username", Arrays.asList("user"));
    requestHeaders.put("password", Arrays.asList("pass"));

		requestContext.put(MessageContext.HTTP_REQUEST_HEADERS, requestHeaders);

    BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) );
    int currentState = -1;

    Thread thr;
    while( true ) {
      switch( currentState ) {

        case -1:
          System.out.println( "\n1. Show all Metallica members" );
          System.out.println( "2. Search member" );
          System.out.println( "3. Add member" );
          System.out.println( "4. Update member" );
          System.out.println( "5. Delete member" );
          System.out.println( "0. Exit" );
          currentState = readState( currentState, reader );
          break;

        case 1:
          System.out.println( "Found:" );
          thr = new Thread( new Runnable() 
          {
            public void run() {
              try {
                metallicaPort.findAll( ).stream( ).map( Client::metallicaToString ).forEach( System.out::println );
              }
              catch( MetallicaServiceException ex ) {
                System.out.println( ex.getFaultInfo( ) );
              }
            }
          });
          thr.start( );

          currentState = -1;
          break;

        case 2:
          System.out.println("id:");
          Long id = readLong( reader );

          System.out.println( "name:" );
          String name = readString( reader );

          System.out.println( "instrument:" );
          String instrument = readString( reader );

          System.out.println( "entrydate(yyyy-mm-dd):" );
          XMLGregorianCalendar entrydate = readDate( reader );

          System.out.println( "networth:" );
          Integer networth = readInteger( reader );

          System.out.println( "birthdate(yyyy-mm-dd):" );
          XMLGregorianCalendar birthdate = readDate( reader );

          System.out.println( "Found:" );

          thr = new Thread( new Runnable() 
          {
            public void run() {
              try {
                metallicaPort.findWithFilters( id, name, instrument, entrydate, networth, birthdate ).stream( ).map( Client::metallicaToString ).forEach( System.out::println );
              }
              catch( MetallicaServiceException ex ) {
                System.out.println( ex.getFaultInfo( ) );
              }
            }
          });
          thr.start( );

          currentState = -1;

          break;

        case 3:
          System.out.println( "name:" );
          String c_name = readString( reader );
          if( c_name == null ) {
            System.out.println( "Value is invalid or empty" );
            currentState = -1;
            break;
          }
          
          System.out.println( "instrument:" );
          String c_instrument = readString( reader );
          if( c_instrument == null ) {
            System.out.println( "Value is invalid or empty" );
            currentState = -1;
            break;
          }

          System.out.println( "entrydate(yyyy-mm-dd):" );
          XMLGregorianCalendar c_entrydate = readDate( reader );
          if( c_entrydate == null ) {
            System.out.println( "Value is invalid or empty" );
            currentState = -1;
            break;
          }

          System.out.println( "networth:" );
          Integer c_networth = readInteger( reader );
          if( c_networth == null ) {
            System.out.println( "Value is invalid or empty" );
            currentState = -1;
            break;
          }

          System.out.println( "birthdate(yyyy-mm-dd):" );
          XMLGregorianCalendar c_birthdate = readDate( reader );

          Long new_id = null;
          try {
            new_id = metallicaPort.create( c_name, c_instrument, c_entrydate, c_networth, c_birthdate );
          }
          catch( MetallicaServiceException ex ) {
            System.out.println( ex.getFaultInfo( ) );
          } 
          if( new_id != 0 ) {
            System.out.println( "New ID " + new_id.toString( ) );
          }

          currentState = -1;
        break;

        case 4:
          System.out.println( "id:" );
          Long u_id = readLong( reader );
          if( u_id == null ) {
            System.out.println( "Value is invalid or empty" );
            currentState = -1;
            break;
          }

          System.out.println( "name:" );
          String u_name = readString( reader );

          System.out.println( "instrument:" );
          String u_instrument = readString( reader );

          System.out.println( "entrydate(yyyy-mm-dd):" );
          XMLGregorianCalendar u_entrydate = readDate( reader );

          System.out.println( "networth:" );
          Integer u_networth = readInteger( reader );

          System.out.println( "birthdate(yyyy-mm-dd):" );
          XMLGregorianCalendar u_birthdate = readDate( reader );

          int u_ret = 0;
          try {
            u_ret = metallicaPort.update( u_id, u_name, u_instrument, u_entrydate, u_networth, u_birthdate );
          }
          catch( MetallicaServiceException ex ) {
            System.out.println( ex.getFaultInfo( ) );
          } 
          if( u_ret != 0 ) {
            System.out.println( "OK" );
          }

          currentState = -1;
          break;   

        case 5:
          System.out.println( "id:" );
          Long d_id = readLong( reader );

          int d_ret = 0;
          try {
            d_ret = metallicaPort.delete( d_id );
          } 
          catch( MetallicaServiceException ex ) {
            System.out.println( ex.getFaultInfo( ) );
          } 
          if( d_ret != 0 ) {
            System.out.println( "OK" );
          }

          currentState = -1;
          break;

        case 0:
          return;

        default:
          currentState = -1;
          break;
      }
    }
  }

  private static String readString( BufferedReader reader ) throws IOException {
    String trim = reader.readLine( ).trim( );
    if( trim.isEmpty( ) ) {
      return null;
    }
    return trim;
  }

  private static XMLGregorianCalendar readDate( BufferedReader reader ) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );

      Date rd = sdf.parse( reader.readLine( ) );
      XMLGregorianCalendar birthdate;

      GregorianCalendar c = new GregorianCalendar( );

      if( rd != null ) {
        c.setTime( rd );
        return DatatypeFactory.newInstance( ).newXMLGregorianCalendar( c );
      } else {
        return null;
      }
    } catch( java.lang.Exception e ) {
      return null;
    }
  }

  private static Long readLong( BufferedReader reader ) {
    try {
      return Long.parseLong( reader.readLine( ) );
    } catch( java.lang.Exception e ) {
      return null;
    }
  }

  private static Integer readInteger( BufferedReader reader ) {
    try {
      return Integer.parseInt( reader.readLine( ) );
    } catch( java.lang.Exception e ) {
      return null;
    }
  }

  private static int readState( int current, BufferedReader reader ) {
    try {
      return Integer.parseInt( reader.readLine( ) );
    } catch( java.lang.Exception e ) {
      return current;
    }
  }

  private static String metallicaToString( Metallica metallica ) {
    return metallica.getId( ) + ". " +
      metallica.getName( ) + ", " +
      "plays: " + metallica.getInstrument( ) + ", " + 
      "entered: " + metallica.getEntrydate( ) + ", " +
      "networth: " + metallica.getNetworth( ) + ", " +
      "birthdate: " + metallica.getBirthdate( );
  }

}
