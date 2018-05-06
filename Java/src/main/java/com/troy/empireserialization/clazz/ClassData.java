package com.troy.empireserialization.clazz;

import java.lang.reflect.*;
import java.util.*;

import com.troy.empireserialization.ClassIDProvider;
import com.troy.empireserialization.EmpireOpCodes;
import com.troy.empireserialization.SerializationSettings;
import com.troy.empireserialization.charset.EmpireCharsets;
import com.troy.empireserialization.io.out.*;
import com.troy.empireserialization.serializers.FieldType;
import com.troy.empireserialization.util.*;

import sun.misc.*;

public class ClassData<T> {
	private static final Unsafe unsafe = MiscUtil.getUnsafe();

	private final Class<?> type;

	public Field[] rawFields;
	public String[] fieldNames;
	public Class<?>[] fieldTypes;
	public long[] fieldOffsets;
	public FieldType[] myFieldTypes;

	private byte[] typeDefinition;

	public ClassData(Class<T> type, ClassIDProvider provider) {
		this(type, SerializationSettings.defaultSettings, provider);
	}

	public ClassData(Class<T> type, SerializationSettings settings, ClassIDProvider provider) {
		this.type = type;
		init(settings, provider);
	}

	private void init(SerializationSettings settings, ClassIDProvider provider) {
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
		writeTypeDefinition(settings, provider);
	}

	private boolean isValidField(Field field) {
		int mods = field.getModifiers();
		return !Modifier.isStatic(mods) && !Modifier.isTransient(mods);
	}

	private void writeTypeDefinition(SerializationSettings settings, ClassIDProvider provider) {
		int length = rawFields.length;
		int bitFieldBytes = (length + 3) / 4;
		ByteArrayOutput out = new ByteArrayOutput(100 + bitFieldBytes);
		byte[] bitfield = out.getBuffer();

		EmpireCharsets.write(type.getName(), out);
		out.writeVLEInt(length);
		for (int i = 0; i < length; i++) {
			Field field = rawFields[i];
			int shift = 6 - (i % 4) * 2;
			FieldType type = myFieldTypes[i];
			bitfield[out.getBufferPosition() + i / 4] |= (type.getCode() << shift);
		}
		out.setBufferPosition(out.getBufferPosition() + bitFieldBytes);
		for (int i = 0; i < length; i++) {
			Class<?> fieldType = fieldTypes[i];
			FieldType myType = myFieldTypes[i];
			switch (myType) {
			case PRIMITIVE:
				out.writeByte((settings.useVLE ? EmpireOpCodes.PRIMITIVE_TYPE_VLE_MAPPING
						: EmpireOpCodes.PRIMITIVE_TYPE_MAPPING).get(fieldType));
				break;
			case USER_DEFINED:
				out.writeVLEInt(provider.getTypeID(fieldType));
				break;
			case WILDCARD:
				break;
			default:
				throw new Error();
			}
			EmpireCharsets.write(fieldNames[i], out);
		}

		// Write it
		typeDefinition = Arrays.copyOf(out.getBuffer(), out.getBufferPosition());
		out.close();
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

	public byte[] getTypeDefinition() {
		return typeDefinition;
	}

	public Field[] getFields() {
		return rawFields;
	}
}
