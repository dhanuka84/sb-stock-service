package com.sb.stock.service.utils;

@FunctionalInterface
public interface StockPatcher {

    boolean authorize(int val);
    
    default boolean authorize(String value) {
        return true;
    }
}
