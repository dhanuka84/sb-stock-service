package com.sb.stock.service.web.mappers;

import org.mapstruct.Mapper;

import com.sb.stock.model.StockDto;
import com.sb.stock.service.domain.Stock;

@Mapper(uses = { DateMapper.class }, componentModel = "spring")
public interface StockMapper {

	StockDto stockToDto(Stock stock);

	Stock dtoToStock(StockDto dto);

}
