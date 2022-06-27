package com.sb.stock.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer{
    
    @Autowired
    private JsonPatchHttpMessageConverter jsonPatch;
    
    /*
     * if this registered,
     * You would need an array instead of json object for http patch request
     * 
     * public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
     * System.out.println(); configurer.customCodecs().register(jsonPatch); }
     */
}
