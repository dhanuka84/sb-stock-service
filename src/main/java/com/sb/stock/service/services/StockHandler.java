package com.sb.stock.service.services;

import java.time.Duration;

import org.springframework.stereotype.Component;

import com.sb.stock.service.domain.Stock;
import com.sb.stock.service.exception.NotFound;
import com.sb.stock.service.repositories.StockRepository;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
class StockHandler {

    private final StockRepository posts;

    public Multi<Stock> all() {
        //return posts.findAll().convert().with(toUni()).flatMapMany(Flux::fromIterable);
        return posts.findAll().onItem().disjoint();
    }

    public Uni<Stock> create(Stock stock) {
        return posts.save(stock);
    }
    
    public Multi<Stock> listStocks(int offset, int limit) {
	return posts.findByKeyword(offset, limit, null).onItem().disjoint();
    }

    public Uni<Stock> get(Long id) {
        return this.posts.findById(id)
            .onFailure().invoke(error -> log.error("Got error: "+ error.getMessage()))
            .ifNoItem().after(Duration.ofMillis(10)).failWith(new NotFound("No any stock"));
    }

    public Uni<Stock> update(Stock stock) {
	return posts.update(stock);
    }

    public Uni<Long> delete(Long id) {
        return this.posts.deleteById(id);
          
    }
    
    public Uni<Void> delete(Stock stock) {
        return this.posts.delete(stock);
          
    }
}
