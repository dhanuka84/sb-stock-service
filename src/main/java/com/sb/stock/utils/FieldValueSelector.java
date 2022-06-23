package com.sb.stock.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import org.springframework.util.ReflectionUtils;

public class FieldValueSelector {

    public static void applyCorrectFieldValue(final Object key, final Object value, final Class<?> clazz,
	    final Object entity) {
	Field field = ReflectionUtils.findField(clazz, (String) key);
	field.setAccessible(true);

	switch (field.getType().getCanonicalName()) {
	case "java.math.BigDecimal":
	    ReflectionUtils.setField(field, entity, new BigDecimal((String) value));
	    break;
	case "java.lang.String:class":
	    ReflectionUtils.setField(field, entity, (String) value);
	    break;
	case "java.lang.Long":
	    ReflectionUtils.setField(field, entity, Long.valueOf((String) value));
	    break;
	    
	
	}
    }

}
