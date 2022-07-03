package com.sb.stock.service.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.sb.stock.service.domain.Stock;

import reactor.core.publisher.Flux;

public interface StockRepository extends ReactiveCrudRepository<Stock, Long> {
    
    @Query(value = "SELECT * FROM stock " + "LIMIT :limit OFFSET :offset")
    Flux<Stock> paginate(Long offset, Integer limit);

	
}
