package com.sb.stock.service.web.mappers;

import org.springframework.stereotype.Component;

import com.sb.stock.model.StockDto;
import com.sb.stock.service.domain.Stock;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class StockReactiveMapper {

    private final StockMapper stockMapper;

    public Flux<StockDto> toDTO(Flux<Stock> tweet) {
	return tweet.map(stockMapper::stockToDto);
    }

    public Flux<Stock> toEntity(Flux<StockDto> tweetDTO) {
	return tweetDTO.map(stockMapper::dtoToStock);
    }

    public Mono<StockDto> toDTO(Mono<Stock> tweet) {
	return tweet.map(stockMapper::stockToDto);
    }

    public Mono<Stock> toEntity(Mono<StockDto> tweetDTO) {
	return tweetDTO.map(stockMapper::dtoToStock);
    }
    
    public Stock toEntity(StockDto dto) {
	return stockMapper.dtoToStock(dto);
    }
    
    public StockDto toDTO(Stock entity) {
	return stockMapper.stockToDto(entity);
    }

}
