
package com.sb.stock;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.r2dbc.core.DatabaseClient;

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
	
	/*
	 * @Bean
	 * 
	 * @Qualifier("pgConnectionFactory") public ConnectionFactory
	 * pgConnectionFactory() { return new PostgresqlConnectionFactory(
	 * PostgresqlConnectionConfiguration.builder() .host("localhost")
	 * .database("blogdb") .username("user") .password("password")
	 * //.codecRegistrar(EnumCodec.builder().withEnum("post_status",
	 * Post.Status.class).build()) .build() ); }
	 */

	    @Bean
	    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

	        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
	        initializer.setConnectionFactory(connectionFactory);

	        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
	        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
	       
	        initializer.setDatabasePopulator(populator);

	        return initializer;
	    }

	    @Bean
	    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
	        return builder -> {
	            builder.serializationInclusion(JsonInclude.Include.NON_EMPTY);
	            builder.featuresToDisable(
	                    SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
	                    SerializationFeature.FAIL_ON_EMPTY_BEANS,
	                    DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
	                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	            builder.featuresToEnable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
	        };
	    }
	
}
