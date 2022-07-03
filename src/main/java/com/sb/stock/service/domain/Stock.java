package com.sb.stock.service.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("stock")
public class Stock {

	@Id
	private Long id;

	@Version
	@Column("id")
	private Long version;
	
	@Column("id")
	private String name;

	@Column("last_update")
	private Timestamp lastUpdate;

	@Column("current_price")
	private BigDecimal currentPrice;
	
	@Column("db_update_time")
	private Timestamp dbUpdateTime;

}
