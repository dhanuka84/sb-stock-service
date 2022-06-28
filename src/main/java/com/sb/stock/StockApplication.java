
package com.sb.stock;

import javax.persistence.Persistence;

import org.hibernate.reactive.mutiny.Mutiny;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.info.*;
import io.swagger.v3.oas.models.info.*;
import reactor.netty.http.server.HttpServer;

//@EnableFeignClients //needs to be on SBA class
//@OpenAPIDefinition(info = @Info(title = "Spring webflux crud example", version = "1.0", description = "sample documents"))
//@EnableWebFlux
@Configuration
@ComponentScan
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = false)
@SpringBootApplication
public class StockApplication {
    @Value("${server.port:8080}")
    private int port = 8080;

    public static void main(String[] args) throws Exception {
	try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
		StockApplication.class)) {
	    context.getBean(HttpServer.class).bindNow().onDispose().block();
	}
    }

    @Profile("default")
    @Bean
    public HttpServer nettyHttpServer(ApplicationContext context) {
	HttpHandler handler = WebHttpHandlerBuilder.applicationContext(context).build();
	ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(handler);
	return HttpServer.create().host("localhost").port(this.port).handle(adapter);
    }

    @Bean
    public Mutiny.SessionFactory sessionFactory() {
	return Persistence.createEntityManagerFactory("blogPU").unwrap(Mutiny.SessionFactory.class);
    }

    /*
     * @Bean public GroupedOpenApi stockOpenApi(@Value("${springdoc.version}")
     * String appVersion) { String[] paths = { "/api/**" }; return
     * GroupedOpenApi.builder().group("api").packagesToScan(
     * "com.sb.stock.service.web.controllers") .addOpenApiCustomiser(openApi ->
     * openApi.info(new Info().title("Stock API").version(appVersion)))
     * .pathsToMatch(paths).build(); }
     */

}
