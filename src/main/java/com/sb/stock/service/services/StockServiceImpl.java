package com.sb.stock.service.services;


import java.sql.Timestamp;

import javax.json.JsonPatch;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.stock.model.StockDto;
import com.sb.stock.service.domain.Stock;
import com.sb.stock.service.web.mappers.StockMapper;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

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
    public Uni<StockDto> createStock(final Uni<StockDto> stockDto) {
	log.debug("================ Placing a Stock ============",stockDto);
	return  stockDto
		.map(stockMapper::dtoToStock)
                .flatMap(handler::create)
                .map(stockMapper::stockToDto);
    }
    
    @Override
    public Multi<StockDto> listStocks(int pageNumber, int size) {
	Multi<Stock> stockPage = handler.listStocks(pageNumber,size);
	return stockPage.map(stockMapper::stockToDto);

	/*
	 * return new StockPagedList(stockPage .stream() .map(stockMapper::stockToDto)
	 * .collect(Collectors.toList()), PageRequest.of(
	 * stockPage.getPageable().getPageNumber(),
	 * stockPage.getPageable().getPageSize()), stockPage.getTotalElements());
	 */
    }
    



    @Override
    public Multi<StockDto> getStocks() {
	return handler.all().map(stockMapper::stockToDto);
    }

    @Override
    public Uni<Void> deleteStock(Long stockId) {	
	return handler.get(stockId).map(Stock::getId)
		.flatMap(handler::deleteById).replaceWithVoid();
	
    }
    

    @Override
    public Uni<StockDto> getStocksById(Long stockId) {
	return handler.get(stockId).map(stockMapper::stockToDto);
    }

    @Override 
    public Uni<StockDto> updatePriceById(Long stockId, JsonPatch patchDocument) {
	Uni<Stock> stock = handler.get(stockId);
	return stock
		.map(i -> applyPatchToStock(patchDocument, i) )
		.flatMap(this::updateStock);
    }
    
    private Stock applyPatchToStock(JsonPatch patchDocument, Stock targetStock) {
	log.debug("original stock  {}", targetStock);

        //Converts the original user to a JsonStructure
        JsonStructure target = objectMapper.convertValue(targetStock, JsonStructure.class);
        //Applies the patch to the original stock
        JsonValue pachedStock = patchDocument.apply(target);

        //Converts the JsonValue to a User instance
        Stock modifiedStock = objectMapper.convertValue(pachedStock, Stock.class);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        modifiedStock.setLastUpdate(timestamp);
        return modifiedStock;
    }
    
    
    private Uni<StockDto> updateStock(final Stock stock) {
	log.debug("modified stock {}", stock);
	Uni<Stock> updated = handler.update(stock);
	return updated.map(stockMapper::stockToDto);
    }

}
