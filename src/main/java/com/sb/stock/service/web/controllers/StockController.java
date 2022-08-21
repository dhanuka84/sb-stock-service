package com.sb.stock.service.web.controllers;

import static io.smallrye.mutiny.converters.uni.UniReactorConverters.toMono;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.stock.model.StockDto;
import com.sb.stock.service.services.StockService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
/*
 * @RequestMapping("/api/")
 * 
 * @RestController
 */
public class StockController {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 5;
    
    private final StockService stockService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    public Mono<ServerResponse> createStock(ServerRequest req) {

	return req.bodyToMono(StockDto.class)
		.flatMap(dto -> this.stockService.createStock(dto).convert().with(toMono()))
		.flatMap(p -> ServerResponse.created(URI.create("stocks/" + p.getId())).build());

    }

    
    /*
     * @GetMapping(path = "stocks", produces = "application/json")
     * 
     * @ApiResponses(value = { @ApiResponse(responseCode = "200", description =
     * "create Tweets") })
     */
    public Mono<ServerResponse> listStocks(ServerRequest req) {
   // public Uni<List<StockDto>> listStocks(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
	    			    // @RequestParam(value = "pageSize", required = false) Integer pageSize){
	
	Optional<String> pageNumber = req.queryParam("pageNumber");
	Optional<String> pageSize = req.queryParam("pageSize");
	
	int number = pageNumber.isPresent() ? Integer.valueOf(pageNumber.get()) : DEFAULT_PAGE_NUMBER;
	int size = pageSize.isPresent() ? Integer.valueOf(pageSize.get()) : DEFAULT_PAGE_SIZE;

        if (number < 0){
            number = DEFAULT_PAGE_NUMBER;
        }

        if (size < 1) {
            size = DEFAULT_PAGE_SIZE;
        }

        return ServerResponse.ok()
        		     .body(stockService.listStocks(number,size).convert().with(toMono()), StockDto.class);
    }


    /*
     * @GetMapping(path = "stocks/{stockId}", produces = "application/json")
     * 
     * @ResponseStatus(HttpStatus.OK)
     
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "create Tweets") })*/
    public Mono<ServerResponse> getStocksById(ServerRequest req) {
	return stockService.getStocksById(Long.valueOf(req.pathVariable("stockId")))
			   .convert().with(toMono())
			   .doOnError(error -> log.error("Got error: "+ error.getMessage()))
			   .flatMap(post -> ServerResponse.ok().body(Mono.just(post), StockDto.class));
    }

    /*
     * @PatchMapping(path = "stocks/{stockId}", consumes =
     * "application/json-patch+json", produces = "application/json")
     * 
     * @ResponseStatus(HttpStatus.OK)
     * 
     * @ApiResponses(value = { @ApiResponse(responseCode = "200", description =
     * "create Tweets") }) public Mono<ServerResponse>
     * updateStocksById(@PathVariable("stockId") Long stockId,
     * 
     * @RequestBody final Map<Object,Object> fields) {
     */
    public Mono<ServerResponse> updateStocksById(ServerRequest req) {
	final Long id = Long.valueOf(req.pathVariable("stockId"));
	return req.bodyToMono(Map.class)
		.flatMap(fields -> this.stockService.updatePriceById(id, fields).convert().with(toMono()))
		.flatMap(post -> ServerResponse.ok().body(Mono.just(post), StockDto.class));
	 
    }

    

    /*
     * @DeleteMapping("stocks/{stockId}")
     * 
     * @ResponseStatus(HttpStatus.NO_CONTENT)
     * 
     * @ApiResponses(value = { @ApiResponse(responseCode = "200", description =
     * "create Tweets") })
     */
    public Mono<ServerResponse> deleteStocks(ServerRequest req) {
	return stockService.deleteStock(Long.valueOf(req.pathVariable("stockId")))
		.convert().with(toMono())
		.doOnError(error -> log.error("Got error: "+ error.getMessage()))
		.flatMap(d -> ServerResponse.noContent().build());
		
    }

}
