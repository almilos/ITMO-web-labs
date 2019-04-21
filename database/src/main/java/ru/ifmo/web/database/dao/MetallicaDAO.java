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
import java.sql.Statement;
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
  private final Connection conn;

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
    log.info( "Find all" );
    java.sql.Statement statement = conn.createStatement( );
    StringBuilder query = new StringBuilder( );

    statement.execute( query.append( "SELECT " ).append( String.join( ", ", columnNames ) ).append( " FROM " ).append( TABLE_NAME ).toString( ) );

    List<Metallica> result = resultSetToList( statement.getResultSet( ) );
    return result;
  }

  public List<Metallica> findWithFilters( Long id, String name, String instrument, Date entrydate, Integer networth, Date birthdate ) throws SQLException {
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

    log.debug( query.toString( ) );

    PreparedStatement ps = conn.prepareStatement( query.toString( ) );
    fillPreparedStatement( ps, statements );
    ResultSet rs = ps.executeQuery( );
    return resultSetToList( rs );

  }

  public Long create( String name, String instrument, Date entrydate, Integer networth, Date birthdate ) throws SQLException {

    conn.setAutoCommit( false );

    StringBuilder query = new StringBuilder( );

    query.append( "INSERT INTO " ).append( TABLE_NAME ).append( "(" ).append( String.join( ",", columnNames ) ).append( ") VALUES(?,?,?,?,?,?)" );
    long newId;

    try( java.sql.Statement idStatement = conn.createStatement( ) ) {
      idStatement.execute( "SELECT nextval('metallica_id_seq') nextval" );

      try( ResultSet rs = idStatement.getResultSet( ) ) {
        rs.next( );
        newId = rs.getLong( "nextval" );
      }

      try( PreparedStatement stmnt = conn.prepareStatement( query.toString( ) ) ) {
        stmnt.setLong( 1, newId );
        stmnt.setString( 2, name );
        stmnt.setString( 3, instrument );
        stmnt.setDate( 4, new java.sql.Date( entrydate.getTime( ) ) );
        stmnt.setInt( 5, networth);
        stmnt.setDate( 6, new java.sql.Date( birthdate.getTime( ) ) );
        int count = stmnt.executeUpdate( );

        if( count == 0 ) throw new RuntimeException("SQL query failed");
      }

      conn.commit( );
      conn.setAutoCommit( true );

      return newId;
    }
  }

  public int update( Long id, String name, String instrument, Date entrydate, Integer networth, Date birthdate ) throws SQLException {
    
    if( id == null ) return -1;

    conn.setAutoCommit( true );
    StringBuilder query = new StringBuilder( "UPDATE " + TABLE_NAME + " SET id = id," );
    int i = 1;
    List<Statement> statements = new ArrayList<>();

    if( name != null ) {
      query.append( NAME ).append( "= ?," );
      statements.add( new Statement( i, name, Types.VARCHAR ) );
      i++;
    }

    if( instrument != null ) {
      query.append( INSTRUMENT ).append( "= ?," );
      statements.add( new Statement( i, instrument, Types.VARCHAR ) );
      i++;
    }

    if( entrydate != null ) {
      query.append( ENTRYDATE ).append( "= ?," );
      statements.add( new Statement( i, entrydate, Types.DATE ) );
      i++;
    }

    if( networth != null ) {
      query.append( NETWORTH ).append( "= ?," );
      statements.add( new Statement( i, networth, Types.INTEGER ) );
      i++;
    }

    if( birthdate != null ) {
      query.append( BIRTHDATE ).append( "= ?," );
      statements.add( new Statement( i, birthdate, Types.DATE ) );
      i++;
    }

    query.deleteCharAt( query.length() - 1 );

    statements.add( new Statement( i, id, Types.BIGINT ) );
    query.append(" WHERE id = ?");

    log.debug( query.toString( ) );

    try( PreparedStatement ps = conn.prepareStatement( query.toString( ) ) ) {
      fillPreparedStatement( ps, statements );
      int updated = ps.executeUpdate( );
      return updated;
    }
  }

  public int delete( Long id ) throws SQLException {
    if( id == null ) return -1;

    log.debug("Delete id {}", id);

    conn.setAutoCommit( true );
    try( PreparedStatement ps = conn.prepareStatement( "DELETE FROM " + TABLE_NAME + " WHERE id = ?" ) ) {
      ps.setLong( 1, id );
      return ps.executeUpdate( );
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
