
package com.sb.stock;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

//@EnableFeignClients //needs to be on SBA class
@SpringBootApplication
public class StockApplication {
	public static void main(String[] args) {
		SpringApplication.run(StockApplication.class, args);
	}
	
	/*
	 * @Value("${spring.r2dbc.url}") private String dbUrl;
	 * 
	 * @Bean ConnectionFactoryInitializer
	 * initializer(@Qualifier("connectionFactory") ConnectionFactory
	 * connectionFactory) { ConnectionFactoryInitializer initializer = new
	 * ConnectionFactoryInitializer();
	 * initializer.setConnectionFactory(connectionFactory);
	 * ResourceDatabasePopulator resource = new ResourceDatabasePopulator(new
	 * ClassPathResource("schema.sql")); initializer.setDatabasePopulator(resource);
	 * return initializer; }
	 */
}
