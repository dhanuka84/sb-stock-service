package com.sb.stock.service.services;

import javax.json.JsonPatch;

import org.springframework.validation.annotation.Validated;

import com.sb.stock.model.StockDto;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@Validated
public interface StockService {

    Uni<StockDto> createStock(Uni<StockDto> stockDto);

    Multi<StockDto> getStocks();
    
    Multi<StockDto> listStocks(int pageNumber, int size);

    Uni<Void> deleteStock(Long stockId);

    Uni<StockDto> getStocksById(Long stockId);

    Uni<StockDto> updatePriceById(Long stockId, JsonPatch patchDocument);

}
