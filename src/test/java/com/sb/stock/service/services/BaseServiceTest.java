package com.sb.stock.service.services;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import com.sb.stock.model.StockDto;
import com.sb.stock.service.domain.Stock;
import com.sb.stock.service.repositories.GenericRepository;
import com.sb.stock.service.services.StockService;

public abstract class BaseServiceTest {

	public final long id1 = 1;
	public final long id2 = 2;
	public final long id3 = 3;

	private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
	private static final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	private static final String currentTimeStr = sdf2.format(timestamp);

	@Autowired
	StockService StockService;

	@Autowired
	GenericRepository StockRepository;

	StockDto testStockDto1;
	StockDto testStockDto2;
	StockDto testStockDto3;

	Stock testStock1;
	Stock testStock2;
	Stock testStock3;

	private StockDto buildOrderDto(long uuid) {
		OffsetDateTime offsetDT8 = OffsetDateTime.parse(currentTimeStr);
		return StockDto.builder().id(uuid).lastUpdate(offsetDT8).currentPrice(BigDecimal.valueOf(123.50)).build();
	}

	@BeforeEach
	void setUp() {

		testStockDto1 = buildOrderDto(id1);

		testStockDto2 = buildOrderDto(id2);

		testStockDto3 = buildOrderDto(id3);

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		testStock1 = StockRepository.save(Stock.builder().lastUpdate(timestamp)
				.currentPrice(BigDecimal.valueOf(123.50)).build());
		StockRepository.save(testStock1);

		testStock2 = StockRepository.save(Stock.builder().lastUpdate(timestamp)
				.currentPrice(BigDecimal.valueOf(123.50)).build());
		StockRepository.save(testStock2);

		testStock3 = StockRepository.save(Stock.builder().lastUpdate(timestamp)
				.currentPrice(BigDecimal.valueOf(123.50)).build());
		StockRepository.save(testStock3);

	}
}
