package ru.ifmo.web.standalone;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.ClassNamesResourceConfig;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.grizzly.http.server.HttpServer;
import ru.ifmo.web.service.MetallicaService;
import ru.ifmo.web.service.exception.MetallicaInternalExceptionMapper;
import ru.ifmo.web.service.exception.MetallicaIdNotFoundExceptionMapper;

import java.io.IOException;

@Slf4j
public class App {
  public static void main( String... args ) throws IOException {

    String url = "http://0.0.0.0:8080";

    ClassNamesResourceConfig config = new ClassNamesResourceConfig(MetallicaService.class, MetallicaInternalExceptionMapper.class, MetallicaIdNotFoundExceptionMapper.class);
    log.info("Done creating configs");

    HttpServer server = GrizzlyServerFactory.createHttpServer(url, config);
    log.info("Done creating server");

    log.info("Starting server");
    server.start();

    Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
    log.info("App started");

    System.in.read();
  }


}
