package com.troy.serialization.serializers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import com.troy.serialization.io.Input;
import com.troy.serialization.io.Output;
import com.troy.serialization.util.MiscUtil;

import sun.misc.Unsafe;

public class FieldSerializer<T> extends AbstractSerializer<T> {
	private static final Unsafe unsafe = MiscUtil.getUnsafe();

	private String[] fieldNames;
	private Class<?>[] fieldTypes;
	private long[] fieldOffsets;

	private static final String[] EMPTY_FIELD_NAMES = new String[0];
	private static final Class<?>[] EMPTY_FIELD_TYPES = new Class[0];
	private static final long[] EMPTY_FIELD_OFFSETS = new long[0];

	public FieldSerializer(Class<T> type) {
		super(type);
		init();
	}

	private void init() {
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
			} else {// There are other classes between out super and the object class. We must
					// account for fields in superclasses
				ArrayList<Field> fieldsList = new ArrayList<Field>();
				for (int i = 0; i < fieldsLength; i++)// Dump all into arraylist
					fieldsList.add(fields[i]);
				while (superType != null && superType != Object.class) {
					fields = superType.getDeclaredFields();
					fieldsLength = fields.length;
					for (int i = 0; i < fieldsLength; i++)// Dump all into arraylist
						fieldsList.add(fields[i]);
					superType = superType.getSuperclass();
				}
				this.fieldNames = new String[fieldsList.size()];
				this.fieldTypes = new Class[fieldsList.size()];
				if (unsafe == null)
					this.fieldOffsets = EMPTY_FIELD_OFFSETS;
				else
					this.fieldOffsets = new long[fieldsList.size()];

				int i = 0;
				for (Field field : fieldsList) {
					addField(field, i++);
				}
			}
		}
	}

	private void setEmptyFields() {
		this.fieldNames = EMPTY_FIELD_NAMES;
		this.fieldTypes = EMPTY_FIELD_TYPES;
		this.fieldOffsets = EMPTY_FIELD_OFFSETS;
	}

	private void addField(Field field, int index) {
		System.out.println("adding field " + field + " index " + index);
		fieldNames[index] = field.getName();
		fieldTypes[index] = field.getType();
		if (unsafe != null)
			fieldOffsets[index] = unsafe.objectFieldOffset(field);
	}

	public T newInstance() {
		return null;
	}

	@Override
	public void writeFields(Object obj, Output out) {

	}

	@Override
	public T readFields(Object obj, Input in) {
		return null;
	}

	@Override
	public String toString() {
		return "FieldSerializer [fieldNames=" + Arrays.toString(fieldNames) + ", fieldTypes=" + Arrays.toString(fieldTypes) + ", fieldOffsets="
				+ Arrays.toString(fieldOffsets) + "]";
	}
	
	

}
