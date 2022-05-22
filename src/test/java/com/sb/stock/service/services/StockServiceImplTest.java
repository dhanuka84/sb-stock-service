package com.sb.stock.service.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import javax.json.Json;
import javax.json.JsonPatch;
import javax.json.JsonPatchBuilder;

import org.junit.Rule;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import com.sb.stock.model.StockDto;
import com.sb.stock.model.StockPagedList;
import com.sb.stock.service.exception.NotFound;

@SpringBootTest
class StockServiceImplTest extends BaseServiceTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    @Order(1)
    void createStock() {

	testStockDto1.setId(null);
	StockDto stock = StockService.createStock(testStockDto1);
	assertThat(stock.getId()).isNotNull();

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
	StockPagedList list = StockService.listStocks(PageRequest.of(1, 2));
	assertEquals(2, list.getSize());
    }

    @Test
    @Order(4)
    void deleteStocks() {
	StockDto stock = StockService.getStocksById(1);
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
	StockDto stock = StockService.createStock(testStockDto3);
	assertThat(stock.getId()).isNotNull();
	
	JsonPatchBuilder builder = Json.createPatchBuilder();
	JsonPatch patch = builder.replace("/currentPrice", "2000").build();;
		 
	StockDto updatedStock = StockService.updatePriceById(id3, patch);
	assertEquals(2000, updatedStock.getCurrentPrice().intValue());

    }
}