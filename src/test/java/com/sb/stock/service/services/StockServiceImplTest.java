package com.sb.stock.service.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Collections;

import org.junit.Rule;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.boot.test.context.SpringBootTest;

import com.sb.stock.model.StockDto;
import com.sb.stock.service.exception.NotFound;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest
class StockServiceImplTest extends BaseServiceTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    @Order(1)
    void createStock() {

	testStockDto1.setId(null);
	Mono<StockDto> stock = StockService.createStock(testStockDto1);
	assertThat(stock.block().getId()).isNotNull();

    }

    @Test
    @Order(2)
    void deleteStockFail() {
	// when
	final Throwable raisedException = catchThrowable(() -> StockService.deleteStock(100));

	// then
	assertThat(raisedException).isInstanceOf(NotFound.class);

    }

    @Test
    @Order(3)
    void testPagination() {
	Flux<StockDto>  list = StockService.listStocks(1, 2);
	assertEquals(2, list.count().block());
    }

    @Test
    @Order(4)
    void deleteStocks() {
	Mono<StockDto> stock = StockService.getStocksById(1);
	assertNotEquals(null, stock);
	StockService.deleteStock(1);
	 //when
	final Throwable raisedException = catchThrowable(() -> StockService.getStocksById(1));
	// then
	assertThat(raisedException).isInstanceOf(NotFound.class);

    }
    
    @Test
    @Order(5)
    void updateStock() {

	testStockDto3.setId(null);
	Mono<StockDto> stock = StockService.createStock(testStockDto3);
	assertThat(stock.block().getId()).isNotNull();
		 
	Mono<StockDto> updatedStock = StockService.updatePriceById(id3, Collections.singletonMap("currentPrice","2000"));
	assertEquals(2000, updatedStock.block().getCurrentPrice().intValue());

    }
}