package com.sb.stock.service.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import static io.r2dbc.spi.ConnectionFactoryOptions.*;
import static io.r2dbc.pool.PoolingConnectionFactoryProvider.MAX_SIZE;
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;

@Configuration
@EnableR2dbcRepositories
public class PostgresConfig extends AbstractR2dbcConfiguration {
    
    @Override
    @Bean
    public ConnectionFactory connectionFactory() {
      ConnectionFactory connectionFactory = ConnectionFactoryBuilder.withUrl("r2dbc:postgresql://localhost:5432/blogdb")
	        .hostname("localhost")
	        .username("user")
	        .password("password")
	        .database("blogdb")	      
	        .build();
      ConnectionPoolConfiguration configuration = ConnectionPoolConfiguration.builder(connectionFactory)
          .maxIdleTime(Duration.ofMinutes(30))
          .initialSize(2)
          .maxSize(10)
          .maxCreateConnectionTime(Duration.ofSeconds(1))
          .build();
      return new ConnectionPool(configuration);
    }

}
