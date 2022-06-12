package com.sb.stock.service.services;


import java.sql.Timestamp;
import java.util.stream.Collectors;

import javax.json.JsonPatch;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.stock.model.StockDto;
import com.sb.stock.model.StockPagedList;
import com.sb.stock.service.domain.Stock;
import com.sb.stock.service.exception.NotFound;
import com.sb.stock.service.web.mappers.StockMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockHandler handler;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Mono<StockDto> createStock(final Mono<StockDto> stockDto) {
	log.debug("================ Placing a Stock ============",stockDto);
	return  stockDto
		.map(stockMapper::dtoToStock)
                .flatMap(handler::create)
                .map(stockMapper::stockToDto);
    }
    
    @Override
    public StockPagedList listStocks(Pageable pageable) {
	Page<Stock> stockPage = handler.findAll(pageable);

        return new StockPagedList(stockPage
                .stream()
                .map(stockMapper::stockToDto)
                .collect(Collectors.toList()), PageRequest.of(
                stockPage.getPageable().getPageNumber(),
                stockPage.getPageable().getPageSize()),
                stockPage.getTotalElements());
    }
    



    @Override
    public Flux<StockDto> getStocks() {
	return handler.all().map(stockMapper::stockToDto);
    }

    @Override
    public Mono<Void> deleteStock(Long stockId) {	
	return handler.get(stockId).flatMap(handler::delete);
	
    }
    
    
    private void deleteStockById(Stock stock) {
	handler.delete(stock);
    }

    @Override
    public Mono<StockDto> getStocksById(Long stockId) {
	return handler.get(stockId).map(stockMapper::stockToDto);
    }

    @Override 
    public StockDto updatePriceById(long stockId, JsonPatch patchDocument) {
	final Stock stock = handler.findById(stockId).orElseThrow(() -> new NotFound(" Stock with id ["+ stockId +"] not found "));
	final Stock modified = applyPatchToStock(patchDocument, stock);
	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	modified.setLastUpdate(timestamp);
	return updateStock(modified);
    }
    
    private Stock applyPatchToStock(JsonPatch patchDocument, Stock targetStock) {
	log.debug("original stock  {}", targetStock);

        //Converts the original user to a JsonStructure
        JsonStructure target = objectMapper.convertValue(targetStock, JsonStructure.class);
        //Applies the patch to the original stock
        JsonValue pachedStock = patchDocument.apply(target);

        //Converts the JsonValue to a User instance
        Stock modifiedStock = objectMapper.convertValue(pachedStock, Stock.class);
        return modifiedStock;
    }
    
    
    private StockDto updateStock(final Stock stock) {
	log.debug("modified stock {}", stock);
	final Stock updated = handler.update(stock);
	return stockMapper.stockToDto(updated);
    }

}
