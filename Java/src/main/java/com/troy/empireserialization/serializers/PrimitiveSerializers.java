package com.troy.empireserialization.serializers;

import com.troy.empireserialization.EmpireOpCodes;
import com.troy.empireserialization.ObjectIn;
import com.troy.empireserialization.ObjectOut;
import com.troy.empireserialization.SerializationSettings;
import com.troy.empireserialization.io.in.Input;
import com.troy.empireserialization.io.out.Output;

public class PrimitiveSerializers {

	static {
		Serializers.addSerializer(new AbstractObjectSerializer<Byte>(Byte.class) {

			@Override
			public void write(ObjectOut objectOut, Byte obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
				if (writeHeader)
					out.writeByte(EmpireOpCodes.SIGNED_BYTE_TYPE);
				out.writeByte(obj.byteValue());
			}

			@Override
			public void read(ObjectIn objIn, Byte obj, Input in, Class<Byte> type, long extra) {
				// TODO
			}
		});

		Serializers.addSerializer(new AbstractObjectSerializer<Short>(Short.class) {

			@Override
			public void write(ObjectOut objectOut, Short obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
				if (writeHeader)
					out.writeByte(EmpireOpCodes.SIGNED_SHORT_TYPE);
				if (settings.useVLE)
					out.writeVLEShort(obj.shortValue());
				else
					out.writeShort(obj.shortValue());
			}

			@Override
			public void read(ObjectIn objIn, Short obj, Input in, Class<Short> type, long extra) {
				// TODO
			}
		});
		Serializers.addSerializer(new AbstractObjectSerializer<Integer>(Integer.class) {

			@Override
			public void write(ObjectOut objectOut, Integer obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
				if (writeHeader)
					out.writeByte(EmpireOpCodes.SIGNED_INT_TYPE);
				if (settings.useVLE)
					out.writeVLEInt(obj.intValue());
				else
					out.writeInt(obj.intValue());
			}

			@Override
			public void read(ObjectIn objIn, Integer obj, Input in, Class<Integer> type, long extra) {
				// TODO
			}
		});
		Serializers.addSerializer(new AbstractObjectSerializer<Long>(Long.class) {

			@Override
			public void write(ObjectOut objectOut, Long obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
				if (writeHeader)
					out.writeByte(EmpireOpCodes.SIGNED_LONG_TYPE);
				out.writeLong(obj.longValue());
			}

			@Override
			public void read(ObjectIn objIn, Long obj, Input in, Class<Long> type, long extra) {
				// TODO
			}
		});
		Serializers.addSerializer(new AbstractObjectSerializer<Float>(Float.class) {

			@Override
			public void write(ObjectOut objectOut, Float obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
				if (writeHeader)
					out.writeByte(EmpireOpCodes.FLOAT_TYPE);
				out.writeFloat(obj.floatValue());
			}

			@Override
			public void read(ObjectIn objIn, Float obj, Input in, Class<Float> type, long extra) {
				// TODO
			}
		});
		Serializers.addSerializer(new AbstractObjectSerializer<Double>(Double.class) {

			@Override
			public void write(ObjectOut objectOut, Double obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
				if (writeHeader)
					out.writeByte(EmpireOpCodes.DOUBLE_TYPE);
				out.writeDouble(obj.doubleValue());
			}

			@Override
			public void read(ObjectIn objIn, Double obj, Input in, Class<Double> type, long extra) {
				// TODO
			}
		});
		Serializers.addSerializer(new AbstractObjectSerializer<Character>(Character.class) {

			@Override
			public void write(ObjectOut objectOut, Character obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
				if (writeHeader)
					out.writeByte(EmpireOpCodes.UNSIGNED_SHORT_TYPE);
				out.writeChar(obj.charValue());
			}

			@Override
			public void read(ObjectIn objIn, Character obj, Input in, Class<Character> type, long extra) {
				// TODO
			}
		});
		Serializers.addSerializer(new AbstractObjectSerializer<Boolean>(Boolean.class) {

			@Override
			public void write(ObjectOut objectOut, Boolean obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
				if (obj.booleanValue())
					out.writeByte(EmpireOpCodes.BOOLEAN_TRUE_CONST);
				else
					out.writeByte(EmpireOpCodes.BOOLEAN_FALSE_CONST);
			}

			@Override
			public void read(ObjectIn objIn, Boolean obj, Input in, Class<Boolean> type, long extra) {
				// TODO
			}
		});

		Serializers.addSerializer(new AbstractObjectSerializer<String>(String.class) {

			@Override
			public void write(ObjectOut objectOut, String obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
				objectOut.writeString(obj);
			}

			@Override
			public void read(ObjectIn objIn, String obj, Input in, Class<String> type, long extra) {
				// TODO
			}
		});

	}

	public static void init() {

	}
}
