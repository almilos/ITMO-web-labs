package ru.ifmo.web.database.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.ifmo.web.database.entity.Metallica;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class MetallicaDAO {
  private final DataSource dataSource;

  private final String TABLE_NAME = "metallica";
  private final String ID =         "id";
  private final String NAME =       "name";
  private final String INSTRUMENT = "instrument";
  private final String ENTRYDATE =  "entrydate";
  private final String NETWORTH =   "networth";
  private final String BIRTHDATE =  "birthdate";

  private final List<String> columnNames = Arrays.asList( ID, NAME, INSTRUMENT, ENTRYDATE, NETWORTH, BIRTHDATE );

  @Data
  @AllArgsConstructor
  private static class Statement {
    private int number;
    private Object value;
    private int sqlType;
  }

  public List<Metallica> findAll( ) throws SQLException {
    log.info("Find all");

    try( Connection connection = dataSource.getConnection( ) ) {
      java.sql.Statement statement = connection.createStatement( );
      StringBuilder query = new StringBuilder( );

      statement.execute( query.append( "SELECT " ).append( String.join( ", ", columnNames ) ).append( " FROM " ).append( TABLE_NAME ).toString( ) );

      List<Metallica> result = resultSetToList( statement.getResultSet( ) );
      return result;
    }
  }

  public List<Metallica> findWithFilters( Long id, String name, String instrument, Date entrydate, Integer networth, Date birthdate ) throws SQLException {
    log.debug( "Find with filters: {} {} {} {} {} {}", id, name, instrument, entrydate, networth, birthdate );
    if( Stream.of( id, name, instrument, entrydate, networth, birthdate ).allMatch( Objects::isNull ) ) {
      return findAll( );
    }

    StringBuilder query = new StringBuilder( );
    query.append( "SELECT " ).append( String.join( ",", columnNames ) ).append( " FROM " ).append( TABLE_NAME ).append( " WHERE " );
    
    int i = 1;
    
    List<Statement> statements = new ArrayList<>( );
    
    if( id != null ) {
      query.append( ID ).append( "= ?" );
      statements.add( new Statement( i, id, Types.BIGINT ) );
      i++;
    }

    if( name != null ) {
      if( !statements.isEmpty( ) ) {
        query.append( " AND " );
      }
      query.append( NAME ).append( "= ?" );
      statements.add( new Statement( i, name, Types.VARCHAR ) );
      i++;
    }

    if( instrument != null ) {
      if( !statements.isEmpty( ) ) {
        query.append( " AND " );
      }
      query.append( INSTRUMENT ).append( "= ?" );
      statements.add( new Statement( i, instrument, Types.VARCHAR ) );
      i++;
    }

    if( entrydate != null ) {
      if( !statements.isEmpty( ) ) {
        query.append( " AND " );
      }
      query.append( ENTRYDATE ).append( "= ?" );
      statements.add( new Statement( i, entrydate, Types.DATE ) );
      i++;
    }

    if( networth != null ) {
      if( !statements.isEmpty( ) ) {
        query.append( " AND " );
      }
      query.append( NETWORTH ).append( "= ?" );
      statements.add( new Statement( i, networth, Types.INTEGER ) );
      i++;
    }

    if( birthdate != null ) {
      if( !statements.isEmpty( ) ) {
        query.append( " AND " );
      }
      query.append( BIRTHDATE ).append( "= ?" );
      statements.add( new Statement( i, birthdate, Types.DATE ) );
    }

    log.debug( "Query {}", query.toString( ) );

    try( Connection connection = dataSource.getConnection( ) ) {
      PreparedStatement ps = connection.prepareStatement( query.toString( ) );
      fillPreparedStatement( ps, statements );
      ResultSet rs = ps.executeQuery( );
      return resultSetToList( rs );
    }

  }

  private List<Metallica> resultSetToList( ResultSet rs ) throws SQLException {
    List<Metallica> result = new ArrayList<>( );
    while( rs.next( ) ) {
      result.add( resultSetToEntity( rs ) );
    }
    return result;
  }

  private Metallica resultSetToEntity( ResultSet rs ) throws SQLException {
    Long id = rs.getLong( ID );
    String name = rs.getString( NAME );
    String instrument = rs.getString( INSTRUMENT );
    Date entrydate = rs.getDate( ENTRYDATE );
    Integer networth = rs.getInt( NETWORTH );
    Date birthdate = rs.getDate( BIRTHDATE );
    return new Metallica( id, name, instrument, entrydate, networth, birthdate );
  }

  private void fillPreparedStatement( PreparedStatement ps, List<Statement> statements ) throws SQLException {
    for( Statement statement : statements ) {
      if( statement.getValue( ) == null ) {
        ps.setNull( statement.number, statement.sqlType );
      } else {
        switch( statement.sqlType ) {
          case Types.BIGINT:
            ps.setLong( statement.number, (Long) statement.getValue( ) );
            break;
          case Types.INTEGER:
            ps.setInt( statement.number, (Integer) statement.getValue( ) );
            break;
          case Types.VARCHAR:
            ps.setString( statement.number, (String) statement.getValue( ) );
            break;
          case Types.TIMESTAMP:
            ps.setDate( statement.number, (java.sql.Date) statement.getValue( ) );
            break;
          default:
            throw new RuntimeException( statement.toString( ) );
        }
      }
    }
  }

}
