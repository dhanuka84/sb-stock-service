package com.sb.stock.service.services;

import java.time.Duration;
import java.util.List;

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
class StockPersistanceHandler {

    private final StockRepository posts;

    public Uni<List<Stock>> all() {
        //return posts.findAll().convert().with(toUni()).flatMapMany(Flux::fromIterable);
        return posts.findAll();
    }

    public Uni<Stock> create(Stock stock) {
        return posts.save(stock);
    }
    
    public Uni<List<Stock>> listStocks(int offset, int limit) {
	return posts.findByKeyword(offset, limit, null);
    }

    public Uni<Stock> get(Long id) {
        return this.posts.findById(id)
            .onFailure().invoke(error -> log.error("Got error: "+ error.getMessage()))
            .ifNoItem().after(Duration.ofMillis(1000)).failWith(new NotFound("No any stock"));
    }

    public Uni<Stock> update(Stock stock) {
	return posts.update(stock);
    }

    public Uni<Long> deleteById(Long id) {
        return this.posts.deleteById(id);
          
    }
    
    public Uni<Void> delete(Stock stock) {
        return this.posts.delete(stock);
          
    }
}
