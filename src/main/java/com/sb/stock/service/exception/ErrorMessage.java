package com.sb.stock.service.exception;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ErrorMessage {
	private int statusCode;
	private OffsetDateTime timestamp;
	private String message;
	private String description;

}
