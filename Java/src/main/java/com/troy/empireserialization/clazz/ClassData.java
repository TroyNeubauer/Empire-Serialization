package com.troy.empireserialization.clazz;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import com.troy.empireserialization.EmpireOutput;
import com.troy.empireserialization.SerializationSettings;
import com.troy.empireserialization.io.out.Output;
import com.troy.empireserialization.serializers.FieldType;
import com.troy.empireserialization.util.MiscUtil;

import sun.misc.Unsafe;

public class ClassData<T> {
	private static final Unsafe unsafe = MiscUtil.getUnsafe();

	private final Class<?> type;

	public Field[] rawFields;
	public String[] fieldNames;
	public Class<?>[] fieldTypes;
	public long[] fieldOffsets;
	public FieldType[] myFieldTypes;

	public ClassData(Class<T> type) {
		this(type, SerializationSettings.defaultSettings);
	}

	public ClassData(Class<T> type, SerializationSettings settings) {
		this.type = type;
		init(settings);
	}

	private void init(SerializationSettings settings) {
		Class<?> superType = type.getSuperclass();
		if (type == null) {
		} else {
			Field[] fields = type.getDeclaredFields();
			int fieldsLength = fields.length;
			if (superType == Object.class) {// We are a child of the object class the only fields are in this class
				if (fieldsLength != 0) {
					int fieldCount = 0;
					for (int i = 0; i < fieldsLength; i++) {
						Field field = fields[i];
						if (isValidField(field))
							fieldCount++;
					}
					this.fieldNames = new String[fieldCount];
					this.fieldTypes = new Class[fieldCount];
					this.fieldOffsets = new long[fieldCount];
					this.rawFields = new Field[fieldCount];
					this.myFieldTypes = new FieldType[fieldCount];
					int realIndex = 0;
					for (int i = 0; i < fieldsLength; i++) {
						Field field = fields[i];
						if (isValidField(field))
							addField(field, realIndex++);
					}

				} else {
					// Nop
					// We dont need to do anything because this class has no fields
				}
			} else {// There are other classes between out super and the object class. We must
					// account for fields in superclasses
				ArrayList<Field> fieldsList = new ArrayList<Field>();
				for (int i = 0; i < fieldsLength; i++) {// Dump all into arraylist
					Field field = fields[i];
					if (isValidField(field))
						fieldsList.add(field);
				}
				while (superType != null && superType != Object.class) {
					fields = superType.getDeclaredFields();
					fieldsLength = fields.length;
					for (int i = 0; i < fieldsLength; i++) {// Dump all into arraylist
						Field field = fields[i];
						if (isValidField(field))
							fieldsList.add(field);
					}
					superType = superType.getSuperclass();
				}
				this.fieldNames = new String[fieldsList.size()];
				this.fieldTypes = new Class[fieldsList.size()];
				this.rawFields = new Field[fieldsList.size()];
				this.myFieldTypes = new FieldType[fieldsList.size()];
				if (unsafe != null)
					this.fieldOffsets = new long[fieldsList.size()];

				int i = 0;
				for (Field field : fieldsList) {
					addField(field, i++);
				}
			}
		}
	}

	private boolean isValidField(Field field) {
		int mods = field.getModifiers();
		return !Modifier.isStatic(mods) && !Modifier.isTransient(mods);
	}

	public void writeTypeDefinition(EmpireOutput out) {
		int length = rawFields.length;
		Output impl = out.getImpl();
		out.writeString(type.getSimpleName());
		impl.writeVLEInt(length);
		for (int i = 0; i < length; i++) {
			out.writeTypeComplete(fieldTypes[i]);
			out.writeString(fieldNames[i]);
		}
	}

	private void addField(Field field, int index) {
		field.setAccessible(true);
		rawFields[index] = field;
		fieldNames[index] = field.getName();
		fieldTypes[index] = field.getType();
		myFieldTypes[index] = FieldType.identifyFieldType(field);
		if (unsafe != null)
			fieldOffsets[index] = unsafe.objectFieldOffset(field);
	}

	public Field[] getFields() {
		return rawFields;
	}
}
