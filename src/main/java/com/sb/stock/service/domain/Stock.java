package com.sb.stock.service.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter


@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Proxy(lazy = false)
public class Stock {

	@Id
	/*
	 * @GeneratedValue(generator = "UUID")
	 * 
	 * @GenericGenerator( name = "UUID", strategy = "org.hibernate.id.UUIDGenerator"
	 * )
	 */

	/*
	 * @Type(type="org.hibernate.type.UUIDCharType")
	 * 
	 * @Column(length = 36, columnDefinition = "varchar(36)", updatable = false,
	 * nullable = false )
	 * 
	 * private UUID id;
	 */
	@GeneratedValue(generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	@Version
	private Long version;
	
	private String name;

	@Column(updatable = true,name="lastUpdate")
	private Timestamp lastUpdate;

	@Column(updatable = true,name="currentPrice")
	private BigDecimal currentPrice;
	
	@CreationTimestamp
	@Column(updatable = true,name="dbUpdateTime")
	private Timestamp dbUpdateTime;

}
