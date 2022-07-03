package com.sb.stock.service.services;

import java.sql.Timestamp;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonPatch;
import javax.json.JsonPatchBuilder;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.stock.model.StockDto;
import com.sb.stock.service.domain.Stock;
import com.sb.stock.service.exception.NotFound;
import com.sb.stock.service.repositories.StockRepository;
import com.sb.stock.service.web.mappers.StockReactiveMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final ObjectMapper objectMapper;
    
    @Autowired
    private StockReactiveMapper stockMapper;

    @Override
    @Transactional
    public Mono<StockDto> createStock(final StockDto stockDto) {
	log.debug("================ Placing a Stock ============", stockDto);	
	return stockMapper
		.toDTO(stockRepository
		.save(stockMapper.toEntity(stockDto))
		.log());
    }

    @Override
    public Flux<StockDto> listStocks(final Integer page, final Integer limit) {

	long offset = (page - 1) * limit;
	return stockMapper
		.toDTO(stockRepository
		.paginate(offset, limit)
		.switchIfEmpty(Flux.error(new NotFound(" No any stock"))))
		.log();

    }

    @Override
    public Flux<StockDto> getStocks() {
	return stockMapper
		.toDTO(stockRepository
		.findAll()
		.switchIfEmpty(Flux.error(new NotFound("No any stock"))))
		.log();
	
	
    }

    @Override
    public Mono<Void> deleteStock(long stockId) {
	return stockRepository
		.findById(stockId)
		.switchIfEmpty(Mono.error(new NotFound("Stock with id [" + stockId + "] not found")))
		.flatMap(existingStock -> stockRepository.delete(existingStock));
    }

    @Override
    public Mono<StockDto> getStocksById(long stockId) {
	return stockMapper
		.toDTO(stockRepository
		.findById(stockId)
		.switchIfEmpty(Mono.error(new NotFound("Stock with id [" + stockId + "] not found"))));
		
    }

    @Override
    // @Transactional
    public Mono<StockDto> updatePriceById(long stockId, Map<Object, Object> fields) {

	final JsonPatchBuilder builder = Json.createPatchBuilder();
	fields.forEach((key, value) -> {
	    // assume all the keys and values are strings
	    builder.add("/" + (String) key, String.valueOf(value));
	});
	
	JsonPatch patch = builder.build();
	
	return stockRepository
	.findById(stockId)
	.switchIfEmpty(Mono.error(new NotFound(" Stock with id [" + stockId + "] not found ")))
	.map(i -> applyPatchToStock(patch, i))
	.flatMap(this::updateStock);
    }
    
    private Stock applyPatchToStock(JsonPatch patchDocument, Stock targetStock) {
	log.debug("original stock  {}", targetStock);

        //Converts the original stock to a JsonStructure
        JsonStructure target = objectMapper.convertValue(targetStock, JsonStructure.class);
        //Applies the patch to the original stock
        JsonValue pachedStock = patchDocument.apply(target);

        //Converts the JsonValue to a stock instance
        Stock modifiedStock = objectMapper.convertValue(pachedStock, Stock.class);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        modifiedStock.setLastUpdate(timestamp);
        return modifiedStock;
    }

    private Mono<StockDto> updateStock(final Stock stock) {
	return stockMapper.toDTO(stockRepository.save(stock)).log();
    }

}
