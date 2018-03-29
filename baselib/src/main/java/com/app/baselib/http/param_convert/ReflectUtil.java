package com.app.baselib.http.param_convert;

import java.lang.reflect.Field;

public class ReflectUtil {

	public static boolean isInt(Field field) {
		return field.getGenericType().toString().equals("int")
				|| field.getGenericType().toString()
						.equals("class java.lang.Integer");
	}

	public static boolean isLong(Field field) {
		return field.getGenericType().toString().equals("long")
				|| field.getGenericType().toString()
				.equals("class java.lang.Long");
	}

	public static boolean isFloat(Field field) {
		return field.getGenericType().toString().equals("float")
				|| field.getGenericType().toString()
						.equals("class java.lang.Float");
	}

	public static boolean isDouble(Field field) {
		return field.getGenericType().toString().equals("double")
				|| field.getGenericType().toString()
				.equals("class java.lang.Double");
	}

	public static boolean isBoolean(Field field) {
		return field.getGenericType().toString().equals("boolean")
				|| field.getGenericType().toString()
						.equals("class java.lang.Boolean");
	}
	
	public static boolean isString(Field field) {
		return field.getGenericType().toString()
						.equals("class java.lang.String");
	}
}
