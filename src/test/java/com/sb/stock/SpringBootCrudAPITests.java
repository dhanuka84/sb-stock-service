package com.sb.stock;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import com.sb.stock.model.StockDto;
import com.sb.stock.service.repositories.StockRepository;

import io.smallrye.mutiny.Uni;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootCrudAPITests {

    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    private static final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    private static final String currentTimeStr = sdf2.format(timestamp);

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    StockRepository stockRepository;

    @BeforeAll
    public static void init() {
	restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp() {
	baseUrl = baseUrl.concat(":").concat(port + "").concat("/api/");
    }

    private StockDto buildOrderDto(long uuid) {
	OffsetDateTime offsetDT8 = OffsetDateTime.parse(currentTimeStr);
	return StockDto.builder().id(uuid).lastUpdate(offsetDT8).currentPrice(BigDecimal.valueOf(123.50)).name("test1").build();
    }

    @Test
    public void testAddStock() {
	long id1 = 1;
	StockDto stock = buildOrderDto(id1);
	StockDto response = restTemplate.postForObject(baseUrl+"stocks", stock, StockDto.class);
	//assertEquals("headset", response.getName());
	assertEquals(true, stockRepository.findAll().toMulti().collect()
	        .with(Collectors.counting()).equals(Uni.createFrom().item(1L)));
    }

}
