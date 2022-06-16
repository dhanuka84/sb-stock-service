
package com.sb.stock;

import javax.persistence.Persistence;

import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
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
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;


import reactor.netty.http.server.HttpServer;

//@EnableFeignClients //needs to be on SBA class
@Configuration
@ComponentScan
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
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
        return Persistence.createEntityManagerFactory("blogPU")
            .unwrap(Mutiny.SessionFactory.class);
    }
}
