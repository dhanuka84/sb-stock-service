
package com.sb.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableFeignClients //needs to be on SBA class
@SpringBootApplication
public class StockApplication {
	public static void main(String[] args) {
		SpringApplication.run(StockApplication.class, args);
	}
}
