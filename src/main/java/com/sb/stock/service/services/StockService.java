package com.sb.stock.service.services;

import java.util.List;
import java.util.Map;

import org.springframework.validation.annotation.Validated;

import com.sb.stock.model.StockDto;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@Validated
public interface StockService {

    Uni<StockDto> createStock(StockDto stockDto);

    Uni<List<StockDto>> getStocks();
    
    Uni<List<StockDto>> listStocks(int pageNumber, int size);

    Uni<Void> deleteStock(Long stockId);

    Uni<StockDto> getStocksById(Long stockId);

    Uni<StockDto> updatePriceById(Long stockId, Map fields);

}
