package com.sb.stock.service.web.controllers;

import javax.json.JsonPatch;
import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sb.stock.model.StockDto;
import com.sb.stock.model.StockPagedList;
import com.sb.stock.service.services.StockService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/")
@RestController
public class StockController {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 5;
    
    private final StockService stockService;

    public StockController(final StockService stockService) {
	this.stockService = stockService;
    }

    @PostMapping(path = "stocks", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody StockDto createStock(@Valid @RequestBody final StockDto stockDto) {

	return stockService.createStock(stockDto);

    }
    
    @GetMapping("stocks")
    public StockPagedList listStocks(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize){

        if (pageNumber == null || pageNumber < 0){
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        return stockService.listStocks(PageRequest.of(pageNumber, pageSize));
    }


    @GetMapping(path = "stocks/{stockId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody StockDto getStocksById(@PathVariable("stockId") Long stockId) {
	return stockService.getStocksById(stockId);
    }

    @PatchMapping(path = "stocks/{stockId}", consumes = "application/json-patch+json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody StockDto updateStocksById(@PathVariable("stockId") Long stockId,
	    @RequestBody JsonPatch patch) {
	return stockService.updatePriceById(stockId,patch);
    }

    

    @DeleteMapping("stocks/{stockId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStocks(@PathVariable("stockId") long stockId) {
	stockService.deleteStock(stockId);
    }

}
