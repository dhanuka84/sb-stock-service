package com.sb.stock.service.web.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.sb.stock.model.StockDto;
import com.sb.stock.service.domain.Stock;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StockReactiveMapper {

    private final StockMapper stockMapper;

    public Uni<List<StockDto>> toDTOList(Uni<List<Stock>> stocks) {
	return stocks.map(list -> list.stream().map(stockMapper::stockToDto).collect(Collectors.toList()));
    }

    public Multi<Stock> toEntity(Multi<StockDto> stockDTO) {
	return stockDTO.map(stockMapper::dtoToStock);
    }

    public Uni<StockDto> toDTO(Uni<Stock> stock) {
	return stock.map(stockMapper::stockToDto);
    }

    public Uni<Stock> toEntity(Uni<StockDto> stockDTO) {
	return stockDTO.map(stockMapper::dtoToStock);
    }

    public Stock toEntity(StockDto dto) {
	return stockMapper.dtoToStock(dto);
    }

    public StockDto toDTO(Stock entity) {
	return stockMapper.stockToDto(entity);
    }

}
