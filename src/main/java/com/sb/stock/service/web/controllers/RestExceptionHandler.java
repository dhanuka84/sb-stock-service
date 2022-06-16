package com.sb.stock.service.web.controllers;

import java.util.Map;

import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.stock.service.exception.NotFound;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Order(-100)
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class RestExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.debug("handling exceptions: {}", ex.getClass().getSimpleName());
        if (ex instanceof NotFound) {
            exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);

            var errors= Map.of("error", ex.getMessage());
            var db = new DefaultDataBufferFactory().wrap(objectMapper.writeValueAsBytes(errors));

            // write the given data buffer to the response
            // and return a Mono that signals when it's done
            return exchange.getResponse().writeWith(Mono.just(db));
        }
        return Mono.error(ex);
    }

}
