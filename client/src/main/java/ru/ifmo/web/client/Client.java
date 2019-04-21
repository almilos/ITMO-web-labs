package ru.ifmo.web.client;

import ru.ifmo.web.database.entity.Metallica;

import javax.xml.datatype.DatatypeFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class Client {
  public static void main( String... args ) throws IOException {
    MetallicaResourceIntegration metallicaPort = new MetallicaResourceIntegration( );

    BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) );
    int currentState = -1;

    Thread thr;
    while( true ) {
      switch( currentState ) {

        case -1:
          System.out.println( "\n1. Show all Metallica members" );
          System.out.println( "2. Search member" );
          /*System.out.println( "3. Add member" );
          System.out.println( "4. Update member" );
          System.out.println( "5. Delete member" );*/
          System.out.println( "0. Exit" );
          currentState = readState( currentState, reader );
          break;

        case 1:
          System.out.println( "Found:" );
          metallicaPort.findAll( ).stream( ).map( Client::metallicaToString ).forEach( System.out::println );

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
          Date entrydate = readDate( reader );

          System.out.println( "networth:" );
          Integer networth = readInteger( reader );

          System.out.println( "birthdate(yyyy-mm-dd):" );
          Date birthdate = readDate( reader );

          System.out.println( "Found:" );

          metallicaPort.findWithFilters( id, name, instrument, entrydate, networth, birthdate ).stream( ).map( Client::metallicaToString ).forEach( System.out::println );

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

  private static Date readDate( BufferedReader reader ) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );

      Date rd = sdf.parse( reader.readLine( ) );
      return rd;

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
