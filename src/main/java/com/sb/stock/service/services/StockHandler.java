package com.sb.stock.service.services;

import static io.smallrye.mutiny.converters.uni.UniReactorConverters.toMono;
import static io.smallrye.mutiny.converters.uni.UniReactorConverters.toFlux;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.sb.stock.service.domain.Stock;
import com.sb.stock.service.exception.NotFound;
import com.sb.stock.service.repositories.StockRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
class StockHandler {

    private final StockRepository posts;

    public Flux<Stock> all() {
        return posts.findAll().convert().with(toMono()).flatMapMany(Flux::fromIterable);
    }

    public Mono<Stock> create(Stock stock) {
        
        return posts.save(stock).convert().with(toMono());
    }

    public Mono<Stock> get(Long id) {
        return this.posts.findById(id).convert().with(toMono())
            .doOnError(error -> log.error("Got error: "+ error.getMessage()))
            .switchIfEmpty(Mono.error(new NotFound(id.toString())));
    }

    public Mono<Stock> update(Stock stock) {

	return posts.update(stock).convert().with(toMono());
    }

    public Mono<Long> delete(Long id) {
        return this.posts.deleteById(id).convert().with(toMono());
          
    }
    
    public Mono<Void> delete(Stock stock) {
        return this.posts.delete(stock).convert().with(toMono());
          
    }
}
