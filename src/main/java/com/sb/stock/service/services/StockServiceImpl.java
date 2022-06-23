package com.sb.stock.service.services;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.stock.model.StockDto;
import com.sb.stock.model.StockPagedList;
import com.sb.stock.service.domain.Stock;
import com.sb.stock.service.exception.NotFound;
import com.sb.stock.service.repositories.StockRepository;
import com.sb.stock.service.web.mappers.StockMapper;
import com.sb.stock.utils.FieldValueSelector;

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
    public StockDto updatePriceById(long stockId, Map<Object,Object> fields) {
	final Stock stock = stockRepository.findById(stockId).orElseThrow(() -> new NotFound(" Stock with id ["+ stockId +"] not found "));
	fields.forEach((key,value)->{
	    FieldValueSelector.applyCorrectFieldValue(key, value, Stock.class, stock);
	});
	
	return updateStock(stock);
    }
    
    private StockDto updateStock(final Stock stock) {
	log.debug("modified stock {}", stock);
	final Stock updated = stockRepository.saveAndFlush(stock);
	return stockMapper.stockToDto(updated);
    }
    
    

}
