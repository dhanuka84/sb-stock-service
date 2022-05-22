package com.sb.stock.service.services;

import java.util.List;

import javax.json.JsonPatch;

import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import com.sb.stock.model.StockDto;
import com.sb.stock.model.StockPagedList;

@Validated
public interface StockService {

    StockDto createStock(final StockDto stockDto);

    List<StockDto> getStocks();
    
    StockPagedList listStocks(Pageable pageable);

    void deleteStock(long stockId);

    StockDto getStocksById(long stockId);

    StockDto updatePriceById(long stockId, JsonPatch patchDocument);
}
