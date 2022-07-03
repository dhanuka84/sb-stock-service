package com.sb.stock.service.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import com.sb.stock.model.StockDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Validated
public interface StockService {

    Mono<StockDto> createStock(StockDto stockDto);

    Flux<StockDto> getStocks();
    
    Flux<StockDto> listStocks(Integer page, Integer limit);

    Mono<Void> deleteStock(long stockId);

    Mono<StockDto> getStocksById(long stockId);

    Mono<StockDto> updatePriceById(long stockId, Map<Object,Object> fields);
}
