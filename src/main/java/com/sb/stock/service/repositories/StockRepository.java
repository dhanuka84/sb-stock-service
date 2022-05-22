package com.sb.stock.service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sb.stock.service.domain.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {

	
}
