package com.sb.stock.service.web.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sb.stock.model.StockDto;
import com.sb.stock.service.services.StockService;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(description = "Create a test model demo", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody())
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "create Tweets") })
    public Uni<StockDto> createStock(@Valid @RequestBody StockDto stockDto) {

	return stockService.createStock(Uni.createFrom().item(stockDto));

    }
    
    @GetMapping(path = "stocks", produces = "application/json")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "create Tweets") })
    public Multi<StockDto> listStocks(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
	    			     @RequestParam(value = "pageSize", required = false) Integer pageSize){

        if (pageNumber == null || pageNumber < 0){
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        return stockService.listStocks(pageNumber,pageSize);
    }


    @GetMapping(path = "stocks/{stockId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "create Tweets") })
    public Uni<StockDto> getStocksById(@PathVariable("stockId") Long stockId) {
	return stockService.getStocksById(stockId);
    }

    @PatchMapping(path = "stocks/{stockId}", consumes = "application/json-patch+json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "create Tweets") })
    public Uni<StockDto> updateStocksById(@PathVariable("stockId") Long stockId,
	    @RequestBody final Map<Object,Object> fields) {
	return stockService.updatePriceById(stockId,fields);
    }

    

    @DeleteMapping("stocks/{stockId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "create Tweets") })
    public Uni<Void> deleteStocks(@PathVariable("stockId") long stockId) {
	return stockService.deleteStock(stockId);
    }

}
