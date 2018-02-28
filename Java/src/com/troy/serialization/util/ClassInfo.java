package com.troy.serialization.util;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ClassInfo<T> {

	private Class<T> type;
	private String[] fieldNames;
	private Class<?>[] fieldTypes;
	private long[] fieldOffsets;

	private static final String[] EMPTY_FIELD_NAMES = new String[0];
	private static final Class[] EMPTY_FIELD_TYPES = new Class[0];
	private static final long[] EMPTY_FIELD_OFFSETS = new long[0];

	private ClassInfo(Class<T> type) {
		this.type = type;
		Class<?> superType = type.getSuperclass();
		if (type == null) {
			setEmptyFields();
		} else {
			Field[] fields = type.getDeclaredFields();
			int fieldsLength = fields.length;
			if (superType == Object.class) {// We are a child of the object class the only fields are in this class
				if (fieldsLength == 0) {// We safely know that this object has no fields
					setEmptyFields();
				} else {
					this.fieldNames = new String[fieldsLength];
					this.fieldTypes = new Class[fieldsLength];
					this.fieldOffsets = new long[fieldsLength];
					for (int i = 0; i < fieldsLength; i++)
						addField(fields[i], i);
						
				}
			} else {// There are other classes between out super and the object class. We must account for fields in superclasses
				ArrayList<Field> fieldsList = new ArrayList<Field>();
				for (int i = 0; i < fieldsLength; i++)//Dump all into arraylist
					fieldsList.add(fields[i]);
				while(superType != null && superType != Object.class) {
					fields = superType.getDeclaredFields();
					fieldsLength = fields.length;
					for (int i = 0; i < fieldsLength; i++)//Dump all into arraylist
						fieldsList.add(fields[i]);
				}
				System.out.println("Fields list " + fieldsList);
				
			}
		}
	}

	private void setEmptyFields() {
		this.fieldNames = EMPTY_FIELD_NAMES;
		this.fieldTypes = EMPTY_FIELD_TYPES;
		this.fieldOffsets = EMPTY_FIELD_OFFSETS;
	}

	private void addField(Field field, int index) {

	}

	public T newInstance() {
		try {
			return type.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
