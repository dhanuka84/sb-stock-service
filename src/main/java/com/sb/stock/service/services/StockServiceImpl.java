package com.sb.stock.service.services;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import javax.json.JsonPatch;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.stock.model.StockDto;
import com.sb.stock.model.StockPagedList;
import com.sb.stock.service.domain.Stock;
import com.sb.stock.service.exception.NotFound;
import com.sb.stock.service.repositories.StockRepository;
import com.sb.stock.service.web.mappers.StockMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public StockDto createStock(final StockDto stockDto) {
	log.debug("================ Placing a Stock ============",stockDto);
	final Stock stock = stockMapper.dtoToStock(stockDto);
	stock.setId(null);
	
	stockRepository.saveAndFlush(stock);
	return stockMapper.stockToDto(stock);
    }
    
    @Override
    public StockPagedList listStocks(Pageable pageable) {
	Page<Stock> stockPage = stockRepository.findAll(pageable);

        return new StockPagedList(stockPage
                .stream()
                .map(stockMapper::stockToDto)
                .collect(Collectors.toList()), PageRequest.of(
                stockPage.getPageable().getPageNumber(),
                stockPage.getPageable().getPageSize()),
                stockPage.getTotalElements());
    }
    



    @Override
    public List<StockDto> getStocks() {
	final List<Stock> stats = stockRepository.findAll();
	List<StockDto> list = emptyIfNull(stats).stream().map(v -> stockMapper.stockToDto(v)).collect(Collectors.toList());
	return list;
    }

    @Override
    @Transactional
    public void deleteStock(long stockId) {	
	final Stock stock = stockRepository.findById(stockId).orElseThrow(() -> new NotFound(" Stock with id ["+ stockId +"] not found "));
	deleteStockById(stock);
	
    }
    
    
    private void deleteStockById(Stock stock) {
	stockRepository.delete(stock);
    }

    @Override
    public StockDto getStocksById(long stockId) {
	final Stock stock = stockRepository.findById(stockId).orElseThrow(() -> new NotFound(" Stock with id ["+ stockId +"] not found "));
	return stockMapper.stockToDto(stock);
    }

    @Override   
    @Transactional
    public StockDto updatePriceById(long stockId, JsonPatch patchDocument) {
	final Stock stock = stockRepository.findById(stockId).orElseThrow(() -> new NotFound(" Stock with id ["+ stockId +"] not found "));
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
	final Stock updated = stockRepository.saveAndFlush(stock);
	return stockMapper.stockToDto(updated);
    }

}
