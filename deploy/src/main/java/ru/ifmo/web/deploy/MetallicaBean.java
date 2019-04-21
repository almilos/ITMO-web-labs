package ru.ifmo.web.deploy;

import lombok.Data;
import ru.ifmo.web.database.dao.MetallicaDAO;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.SQLException;

@Data
@ApplicationScoped
public class MetallicaBean {
  @Resource( lookup = "jdbc/metallica" )
  private DataSource ds;

  @Produces
  public MetallicaDAO marineDAO( ) throws SQLException {
    Connection conn = ds.getConnection( );
    return new MetallicaDAO( conn );
  }
}
