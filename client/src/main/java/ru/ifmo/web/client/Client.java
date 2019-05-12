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
import java.util.List;

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
          System.out.println( "3. Add member" );
          System.out.println( "4. Update member" );
          System.out.println( "5. Delete member" );
          System.out.println( "0. Exit" );
          currentState = readState( currentState, reader );
          break;

        case 1:
          System.out.println( "Found:" );

          ReqResult<List<Metallica>> all = metallicaPort.findAll();
          if (all.isErr()) {
            System.out.println(all.getErrorMessage());
          } else {
            all.getResult().stream().map(Client::metallicaToString).forEach(System.out::println);
          }
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

          ReqResult<List<Metallica>> res = metallicaPort.findWithFilters( id, name, instrument, entrydate, networth, birthdate);
          if (res.isErr()) {
            System.out.println(res.getErrorMessage());
          } else {
            res.getResult().stream().map(Client::metallicaToString).forEach(System.out::println);
          }
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
          Date c_entrydate = readDate( reader );
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
          Date c_birthdate = readDate( reader );

          ReqResult<Long> longReqResult = metallicaPort.create( c_name, c_instrument, c_entrydate, c_networth, c_birthdate );
          if (longReqResult.isErr()) {
            System.out.println(longReqResult.getErrorMessage());
          } else {
            System.out.println("New ID: " + longReqResult.getResult());
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
          Date u_entrydate = readDate( reader );

          System.out.println( "networth:" );
          Integer u_networth = readInteger( reader );

          System.out.println( "birthdate(yyyy-mm-dd):" );
          Date u_birthdate = readDate( reader );

          ReqResult<Integer> update = metallicaPort.update( u_id, u_name, u_instrument, u_entrydate, u_networth, u_birthdate );
          if (update.isErr()) {
            System.out.println(update.getErrorMessage());
          } else {
            System.out.println("OK");
          }
          currentState = -1;
          break;   

        case 5:
          System.out.println( "id:" );
          Long d_id = readLong( reader );
          if( d_id == null ) {
            System.out.println( "Value is invalid or empty" );
            currentState = -1;
            break;
          }

          ReqResult<Integer> delete = metallicaPort.delete(d_id);
          if (delete.isErr()) {
            System.out.println(delete.getErrorMessage());
          } else {
            System.out.println("OK");
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
