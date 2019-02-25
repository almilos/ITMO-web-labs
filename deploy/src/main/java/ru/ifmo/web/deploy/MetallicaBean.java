package ru.ifmo.web.deploy;

import lombok.Data;
import ru.ifmo.web.database.dao.MetallicaDAO;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

@Data
@ApplicationScoped
public class MetallicaBean {
  @Resource( lookup = "jdbc/metallica" )
  private DataSource dataSource;

  @Produces
  public MetallicaDAO marineDAO( ) {
    return new MetallicaDAO( dataSource );
  }
}
