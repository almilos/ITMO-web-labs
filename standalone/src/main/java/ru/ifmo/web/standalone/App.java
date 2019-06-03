package ru.ifmo.web.standalone;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.ClassNamesResourceConfig;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.grizzly.http.server.HttpServer;
import ru.ifmo.web.service.MetallicaService;
import ru.ifmo.web.service.AuthFilter;
import ru.ifmo.web.service.exception.MetallicaInternalExceptionMapper;
import ru.ifmo.web.service.exception.MetallicaIdNotFoundExceptionMapper;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.annotation.WebServlet;

@Slf4j
public class App {
  public static void main( String... args ) throws IOException {

    String url = "http://0.0.0.0:8080";

    ResourceConfig config = new PackagesResourceConfig("ru.ifmo.web");
    config.getProperties().put(
      "jersey.config.server.provider.packages",
      "ru.ifmo.web"
    );
    config.getProperties().put(
      "javax.ws.rs.container.ContainerRequestFilter",
      "ru.ifmo.web.service.AuthFilter"
    );

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
