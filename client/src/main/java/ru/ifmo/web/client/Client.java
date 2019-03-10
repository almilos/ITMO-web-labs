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

public class Client {
  public static void main( String... args ) throws SQLException_Exception, IOException {
    URL url = new URL( "http://localhost:8080/metallica?wsdl" );
    Metallica_Service metallicaService = new Metallica_Service( url );
    MetallicaService metallicaPort = metallicaService.getMetallicaServicePort( );

    BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) );
    int currentState = -1;

    Thread thr;
    while( true ) {
      switch( currentState ) {

        case -1:
          System.out.println( "\n1. Show all Metallica members" );
          System.out.println( "2. Search member" );
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
              catch( SQLException_Exception ex ) {
                System.out.println( "SQL request failed" );
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
              catch( SQLException_Exception ex ) {
                System.out.println( "SQL request failed" );
              }
            }
          });
          thr.start( );

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
    return "Metallica(" +
        "name=" + metallica.getName() +
        ", instrument=" + metallica.getInstrument() +
        ", entrydate=" + metallica.getEntrydate() +
        ", networth=" + metallica.getNetworth() +
        ", birthdate=" + metallica.getBirthdate() +
        ")";
  }

}
