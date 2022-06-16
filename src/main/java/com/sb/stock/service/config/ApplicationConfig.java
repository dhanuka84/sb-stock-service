package com.sb.stock.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr353.JSR353Module;

@Configuration
public class ApplicationConfig {

    /*
     * @Bean public ObjectMapper objectMapper() { ObjectMapper objectMapper = new
     * ObjectMapper(); objectMapper.registerModule(new JSR353Module());
     * objectMapper.configure(DeserializationFeature.
     * ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false); JavaTimeModule module = new
     * JavaTimeModule();
     * 
     * module.addSerializer(OffsetDateTime.class, new CustomSerializer(formatter));
     * module.addDeserializer(OffsetDateTime.class, new
     * CustomDeserializer(formatter));
     * 
     * objectMapper.registerModule(module);
     * 
     * return objectMapper; }
     */
}
