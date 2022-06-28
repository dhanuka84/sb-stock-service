package com.sb.stock.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.resource.WebJarsResourceResolver;

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
    
    /*
     * @Override public void addResourceHandlers(ResourceHandlerRegistry registry) {
     * registry.addResourceHandler("/webjars/**")
     * .addResourceLocations("classpath:META-INF/resources"+"/webjars/")
     * .resourceChain(true) .addResolver(new WebJarsResourceResolver());
     * 
     * registry.addResourceHandler("/swagger-ui.html**")
     * .addResourceLocations("classpath:/META-INF/resources/"); }
     */
}
