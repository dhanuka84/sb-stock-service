package com.sb.stock.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockDto {

	/*
	 * @Null private UUID id = null;
	 */
	@Null
	private Long id = null;

	@Null
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer version = null;

	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.[SSSSSS][SSS]x", shape = JsonFormat.Shape.STRING)
	@NotNull(message = "Time stamp is required")
	private OffsetDateTime lastUpdate;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@NotNull(message = "Amount is required")
	private BigDecimal currentPrice;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@NotNull(message = "Name is required")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	private String name;

}
