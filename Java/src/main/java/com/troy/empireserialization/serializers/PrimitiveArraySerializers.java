package com.troy.empireserialization.serializers;

import com.troy.empireserialization.EmpireOpCodes;
import com.troy.empireserialization.ObjectIn;
import com.troy.empireserialization.ObjectOut;
import com.troy.empireserialization.SerializationSettings;
import com.troy.empireserialization.io.in.Input;
import com.troy.empireserialization.io.out.Output;

public class PrimitiveArraySerializers {
	static {
		try {
			// Adds array serializers for all primitive types and all wrapper classes
			Serializers.addSerializer(new AbstractObjectSerializer<byte[]>(byte[].class) {

				@Override
				public void write(ObjectOut objectOut, byte[] obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
					if (writeHeader)
						out.writeByte(EmpireOpCodes.PRIMITIVE_ARRAY_TYPE);
					out.writeVLEInt(obj.length);
					out.writeByte(EmpireOpCodes.PRIMITIVE_TYPE_MAPPING.get(byte.class));
					out.writeBytes(obj);
				}

				@Override
				public void read(ObjectIn objIn, byte[] obj, Input in, Class<byte[]> type, long extra) {
					in.readBytes(obj);
				}

			});
			Serializers.addSerializer(new AbstractObjectSerializer<short[]>(short[].class) {

				@Override
				public void write(ObjectOut objectOut, short[] obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
					if (writeHeader)
						out.writeByte(EmpireOpCodes.PRIMITIVE_ARRAY_TYPE);
					out.writeVLEInt(obj.length);
					out.writeByte(EmpireOpCodes.PRIMITIVE_TYPE_MAPPING.get(short.class));
					out.writeShorts(obj);
				}

				@Override
				public void read(ObjectIn objIn, short[] obj, Input in, Class<short[]> type, long extra) {
					in.readShorts(obj);
				}
			});
			Serializers.addSerializer(new AbstractObjectSerializer<int[]>(int[].class) {

				@Override
				public void write(ObjectOut objectOut, int[] obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
					if (writeHeader)
						out.writeByte(EmpireOpCodes.PRIMITIVE_ARRAY_TYPE);
					out.writeVLEInt(obj.length);
					out.writeByte(EmpireOpCodes.PRIMITIVE_TYPE_MAPPING.get(int.class));
					out.writeInts(obj);
				}

				@Override
				public void read(ObjectIn objIn, int[] obj, Input in, Class<int[]> type, long extra) {
					in.readInts(obj);
				}
			});
			Serializers.addSerializer(new AbstractObjectSerializer<long[]>(long[].class) {

				@Override
				public void write(ObjectOut objectOut, long[] obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
					if (writeHeader)
						out.writeByte(EmpireOpCodes.PRIMITIVE_ARRAY_TYPE);
					out.writeVLEInt(obj.length);
					out.writeByte(EmpireOpCodes.PRIMITIVE_TYPE_MAPPING.get(long.class));
					out.writeLongs(obj);
				}

				@Override
				public void read(ObjectIn objIn, long[] obj, Input in, Class<long[]> type, long extra) {
					in.readLongs(obj);
				}
			});
			Serializers.addSerializer(new AbstractObjectSerializer<float[]>(float[].class) {

				@Override
				public void write(ObjectOut objectOut, float[] obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
					if (writeHeader)
						out.writeByte(EmpireOpCodes.PRIMITIVE_ARRAY_TYPE);
					out.writeVLEInt(obj.length);
					out.writeByte(EmpireOpCodes.PRIMITIVE_TYPE_MAPPING.get(float.class));
					out.writeFloats(obj);
				}

				@Override
				public void read(ObjectIn objIn, float[] obj, Input in, Class<float[]> type, long extra) {
					in.readFloats(obj);
				}
			});

			Serializers.addSerializer(new AbstractObjectSerializer<double[]>(double[].class) {

				@Override
				public void write(ObjectOut objectOut, double[] obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
					if (writeHeader)
						out.writeByte(EmpireOpCodes.PRIMITIVE_ARRAY_TYPE);
					out.writeVLEInt(obj.length);
					out.writeByte(EmpireOpCodes.PRIMITIVE_TYPE_MAPPING.get(double.class));
					out.writeDoubles(obj);
				}

				@Override
				public void read(ObjectIn objIn, double[] obj, Input in, Class<double[]> type, long extra) {
					in.readDoubles(obj);
				}
			});

			Serializers.addSerializer(new AbstractObjectSerializer<char[]>(char[].class) {

				@Override
				public void write(ObjectOut objectOut, char[] obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
					if (writeHeader)
						out.writeByte(EmpireOpCodes.PRIMITIVE_ARRAY_TYPE);
					out.writeVLEInt(obj.length);
					out.writeByte(EmpireOpCodes.PRIMITIVE_TYPE_MAPPING.get(char.class));
					out.writeChars(obj);
				}

				@Override
				public void read(ObjectIn objIn, char[] obj, Input in, Class<char[]> type, long extra) {
					in.readChars(obj);
				}
			});

			Serializers.addSerializer(new AbstractObjectSerializer<boolean[]>(boolean[].class) {

				@Override
				public void write(ObjectOut objectOut, boolean[] obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
					if (writeHeader)
						out.writeByte(EmpireOpCodes.PRIMITIVE_ARRAY_TYPE);
					out.writeVLEInt(obj.length);
					out.writeByte(EmpireOpCodes.PRIMITIVE_TYPE_MAPPING.get(boolean.class));
					out.writeBooleansCompact(obj);
				}

				@Override
				public void read(ObjectIn objIn, boolean[] obj, Input in, Class<boolean[]> type, long extra) {
					in.readBooleansCompact(obj);
				}
			});

			// Yuck wrapper classes!

			Serializers.addSerializer(new AbstractObjectSerializer<Byte[]>(Byte[].class) {

				@Override
				public void write(ObjectOut objectOut, Byte[] obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
					if (writeHeader)
						out.writeByte(EmpireOpCodes.PRIMITIVE_ARRAY_TYPE);
					out.writeVLEInt(obj.length);
					out.writeByte(EmpireOpCodes.PRIMITIVE_TYPE_MAPPING.get(Byte.class));
					out.writeBytes(obj);
				}

				@Override
				public void read(ObjectIn objIn, Byte[] obj, Input in, Class<Byte[]> type, long extra) {
					in.readBytes(obj);
				}

			});
			Serializers.addSerializer(new AbstractObjectSerializer<Short[]>(Short[].class) {

				@Override
				public void write(ObjectOut objectOut, Short[] obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
					if (writeHeader)
						out.writeByte(EmpireOpCodes.PRIMITIVE_ARRAY_TYPE);
					out.writeVLEInt(obj.length);
					out.writeByte(EmpireOpCodes.PRIMITIVE_TYPE_MAPPING.get(Short.class));
					out.writeShorts(obj);
				}

				@Override
				public void read(ObjectIn objIn, Short[] obj, Input in, Class<Short[]> type, long extra) {
					in.readShorts(obj);
				}
			});
			Serializers.addSerializer(new AbstractObjectSerializer<Integer[]>(Integer[].class) {

				@Override
				public void write(ObjectOut objectOut, Integer[] obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
					if (writeHeader)
						out.writeByte(EmpireOpCodes.PRIMITIVE_ARRAY_TYPE);
					out.writeVLEInt(obj.length);
					out.writeByte(EmpireOpCodes.PRIMITIVE_TYPE_MAPPING.get(Integer.class));
					out.writeInts(obj);
				}

				@Override
				public void read(ObjectIn objIn, Integer[] obj, Input in, Class<Integer[]> type, long extra) {
					in.readInts(obj);
				}
			});
			Serializers.addSerializer(new AbstractObjectSerializer<Long[]>(Long[].class) {

				@Override
				public void write(ObjectOut objectOut, Long[] obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
					if (writeHeader)
						out.writeByte(EmpireOpCodes.PRIMITIVE_ARRAY_TYPE);
					out.writeVLEInt(obj.length);
					out.writeByte(EmpireOpCodes.PRIMITIVE_TYPE_MAPPING.get(Long.class));
					out.writeLongs(obj);
				}

				@Override
				public void read(ObjectIn objIn, Long[] obj, Input in, Class<Long[]> type, long extra) {
					in.readLongs(obj);
				}
			});
			Serializers.addSerializer(new AbstractObjectSerializer<Float[]>(Float[].class) {

				@Override
				public void write(ObjectOut objectOut, Float[] obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
					if (writeHeader)
						out.writeByte(EmpireOpCodes.PRIMITIVE_ARRAY_TYPE);
					out.writeVLEInt(obj.length);
					out.writeByte(EmpireOpCodes.PRIMITIVE_TYPE_MAPPING.get(Float.class));
					out.writeFloats(obj);
				}

				@Override
				public void read(ObjectIn objIn, Float[] obj, Input in, Class<Float[]> type, long extra) {
					in.readFloats(obj);
				}
			});

			Serializers.addSerializer(new AbstractObjectSerializer<Double[]>(Double[].class) {

				@Override
				public void write(ObjectOut objectOut, Double[] obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
					if (writeHeader)
						out.writeByte(EmpireOpCodes.PRIMITIVE_ARRAY_TYPE);
					out.writeVLEInt(obj.length);
					out.writeByte(EmpireOpCodes.PRIMITIVE_TYPE_MAPPING.get(Double.class));
					out.writeDoubles(obj);
				}

				@Override
				public void read(ObjectIn objIn, Double[] obj, Input in, Class<Double[]> type, long extra) {
					in.readDoubles(obj);
				}
			});

			Serializers.addSerializer(new AbstractObjectSerializer<Character[]>(Character[].class) {

				@Override
				public void write(ObjectOut objectOut, Character[] obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
					if (writeHeader)
						out.writeByte(EmpireOpCodes.PRIMITIVE_ARRAY_TYPE);
					out.writeVLEInt(obj.length);
					out.writeByte(EmpireOpCodes.PRIMITIVE_TYPE_MAPPING.get(Character.class));
					out.writeChars(obj);
				}

				@Override
				public void read(ObjectIn objIn, Character[] obj, Input in, Class<Character[]> type, long extra) {
					in.readChars(obj);
				}
			});

			Serializers.addSerializer(new AbstractObjectSerializer<Boolean[]>(Boolean[].class) {

				@Override
				public void write(ObjectOut objectOut, Boolean[] obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
					if (writeHeader)
						out.writeByte(EmpireOpCodes.PRIMITIVE_ARRAY_TYPE);
					out.writeVLEInt(obj.length);
					out.writeByte(EmpireOpCodes.PRIMITIVE_TYPE_MAPPING.get(Boolean.class));
					out.writeBooleansCompact(obj);
				}

				@Override
				public void read(ObjectIn objIn, Boolean[] obj, Input in, Class<Boolean[]> type, long extra) {
					in.readBooleansCompact(obj);
				}
			});

			Serializers.addSerializer(new AbstractObjectSerializer<String[]>(String[].class) {

				@Override
				public void write(ObjectOut objectOut, String[] obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
					if (writeHeader)
						out.writeByte(EmpireOpCodes.PRIMITIVE_ARRAY_TYPE);
					out.writeVLEInt(obj.length);
					out.writeByte(EmpireOpCodes.PRIMITIVE_TYPE_MAPPING.get(String.class));
					for (int i = 0; i < obj.length; i++) {
						objectOut.writeString(obj[i]);
					}
				}

				@Override
				public void read(ObjectIn objIn, String[] obj, Input in, Class<String[]> type, long extra) {
					// TODO
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void init() {
		// Invokes static initializer
	}
}
