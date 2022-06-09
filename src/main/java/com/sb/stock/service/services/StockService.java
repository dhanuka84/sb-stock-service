package com.sb.stock.service.services;

import javax.json.JsonPatch;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.validation.annotation.Validated;

import com.sb.stock.model.StockDto;
import com.sb.stock.model.StockPagedList;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Validated
public interface StockService {

    Mono<StockDto> createStock(Mono<StockDto> stockDto);

    Flux<StockDto> getStocks();
    
    StockPagedList listStocks(Pageable pageable);

    Mono<Void> deleteStock(Long stockId);

    Mono<StockDto> getStocksById(Long stockId);

    Mono<StockDto> updatePriceById(Long stockId, JsonPatch patchDocument);
}
