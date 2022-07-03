package com.sb.stock.service.web.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sb.stock.model.StockDto;
import com.sb.stock.service.services.StockService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@RunWith(SpringRunner.class)
@WebMvcTest(StockController.class)
class TestStockControllerTest {

    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    private static final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    private static final String currentTimeStr = sdf2.format(timestamp);

    private StockDto buildStock(long id) {
	OffsetDateTime offsetDT8 = OffsetDateTime.parse(currentTimeStr);
	return StockDto.builder().id(id).lastUpdate(offsetDT8).name("test1").currentPrice(BigDecimal.valueOf(123.50)).build();
    }

    // test properties
    static final String API_ROOT = "/api/";

    @MockBean
    StockService StockService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getStocks() throws Exception {

	BigDecimal val1 = BigDecimal.valueOf(100);
	StockDto stock = buildStock(11);
	Flux<StockDto> page = Flux.just(stock);
	
	given(StockService.listStocks(1,2)).willReturn(page);

	mockMvc.perform(get(API_ROOT + "stocks").accept(MediaType.APPLICATION_JSON))
	.andExpect(status().isOk())
	.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        ;
	

	then(StockService).should().listStocks(1,2);
    }

    @Test
    public void deleteTx() throws Exception {
	mockMvc.perform(delete(API_ROOT + "stocks/1").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());

	then(StockService).should().deleteStock(1);

    }

    @Test
    public void doStockFail() throws Exception {
	StockDto stock = buildStock(1);
	String req = asJsonString(stock);
	System.out.print(req);

	given(StockService.createStock(stock)).willReturn(null);
	MvcResult result = mockMvc
		.perform(post(API_ROOT + "stocks").content(req).contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(content().string(containsString(""))).andReturn();
	
    }

    @Test
    public void createStock() throws Exception {
	StockDto stock = buildStock(1);
	stock.setId(null);
	StockDto output = buildStock(1);
	output.setId(1l);
	String req = asJsonString(stock);
	System.out.print(req);

	given(StockService.createStock(stock)).willReturn(Mono.just(output));
	MvcResult result = mockMvc.perform(post(API_ROOT + "stocks").contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
                .andExpect(status().isOk())
                .andReturn()
                ;
	
	then(StockService).should().createStock(any(StockDto.class));


    }


    public static String asJsonString(final Object obj) {
	try {
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	    objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
	    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	    objectMapper.registerModule(new JavaTimeModule());

	    return objectMapper.writeValueAsString(obj);
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

}