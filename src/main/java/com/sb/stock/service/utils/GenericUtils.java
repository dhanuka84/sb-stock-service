package com.sb.stock.service.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericUtils {

    public Class<?> getGenericClass(int index) {
	Type mySuperclass = getClass().getGenericSuperclass();
	Type tType = ((ParameterizedType) mySuperclass).getActualTypeArguments()[index];
	String className = tType.toString().split(" ")[1];
	try {
	    return Class.forName(className);
	} catch (ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;

    }

}
