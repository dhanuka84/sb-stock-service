package com.sb.stock.service.services;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import com.sb.stock.model.StockDto;
import com.sb.stock.model.StockPagedList;

@Validated
public interface StockService {

    StockDto createStock(StockDto stockDto);

    List<StockDto> getStocks();
    
    StockPagedList listStocks(Pageable pageable);

    void deleteStock(long stockId);

    StockDto getStocksById(long stockId);

    StockDto updatePriceById(long stockId, Map<Object,Object> fields);
}
