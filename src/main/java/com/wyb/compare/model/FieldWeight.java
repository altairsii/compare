package com.wyb.compare.model;

import java.lang.reflect.Field;

import com.wyb.compare.CompareUtil;

public class FieldWeight {

	private Field field;

	public int getWeight(Object obj1, Object obj2) {
		try {
			field.setAccessible(true);
			Object o1 = field.get(obj1);
			Object o2 = field.get(obj2);
			return CompareUtil.getWeights(field, o1, o2);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public FieldWeight() {
	}

	public FieldWeight(Field f) {
		this.field = f;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

}
