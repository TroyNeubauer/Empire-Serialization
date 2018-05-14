package com.troy.empireserialization;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.troy.empireserialization.cache.IntValueCache;
import com.troy.empireserialization.cache.IntStringCache;
import com.troy.empireserialization.charset.EmpireCharset;
import com.troy.empireserialization.charset.EmpireCharsets;
import com.troy.empireserialization.exception.MismatchedInputException;
import com.troy.empireserialization.io.in.Input;
import com.troy.empireserialization.io.in.NativeFileInput;
import com.troy.empireserialization.util.MiscUtil;
import com.troy.empireserialization.util.StringFormatter;

public class EmpireInput implements ObjectIn {

	private Input in;

	private IntStringCache stringCache = new IntStringCache(100, 1.0);
	private IntValueCache<Class<?>> classCache = new IntValueCache<Class<?>>(100, 1.0);
	private IntValueCache<Object> objectCache = new IntValueCache<Object>(200, 1.0);
	private SerializationSettings settings;

	public EmpireInput(Input in) {
		this(in, SerializationSettings.defaultSettings);
	}

	public EmpireInput(File file) {
		this(file, SerializationSettings.defaultSettings);
	}

	public EmpireInput(File file, SerializationSettings settings) {
		this(new NativeFileInput(file), SerializationSettings.defaultSettings);
	}

	public EmpireInput(Input in, SerializationSettings settings) {
		this.in = in;
		this.settings = settings;
	}

	@Override
	public <T> T readObject() {
		int opcode = in.readByte();
		if (opcode == EmpireOpCodes.TYPE_REF_OBJ_DEF_TYPE) {

		} else if (opcode == EmpireOpCodes.OBJ_REF_TYPE) {

		} else if (opcode == EmpireOpCodes.TYPE_DEF_OBJ_DEF_TYPE) {

		} else if (opcode == EmpireOpCodes.NULL_REF_CONST) {
			return null;
		} else if (opcode == EmpireOpCodes.STRING_REF_TYPE) {

		} else if ((opcode & EmpireOpCodes.MAJOR_CODE_MASK) == EmpireOpCodes.STRING_TYPE_MAJOR_CODE) {
			String s = readStringImpl(opcode);

		}

		return null;
	}

	@Override
	public byte readByte() {
		return in.readByte();
	}

	@Override
	public short readShort() {
		return in.readShort();
	}

	@Override
	public int readInt() {
		return in.readInt();
	}

	@Override
	public long readLong() {
		return in.readLong();
	}

	@Override
	public float readFloat() {
		return in.readFloat();
	}

	@Override
	public double readDouble() {
		return in.readDouble();
	}

	@Override
	public char readChar() {
		return in.readChar();
	}

	@Override
	public boolean readBoolean() {
		return in.readBoolean();
	}

	@Override
	public String readString() {
		return readStringImpl(in.readByte());
	}

	private String readStringImpl(int opcode) {
		if ((opcode & EmpireOpCodes.MAJOR_CODE_MASK) != EmpireOpCodes.STRING_TYPE_MAJOR_CODE) {
			throw new MismatchedInputException(
					"Expected a String major opcode " + StringFormatter.toBinaryString(EmpireOpCodes.STRING_TYPE_MAJOR_CODE) + " but instead read "
							+ StringFormatter.toBinaryString(opcode & EmpireOpCodes.MAJOR_CODE_MASK));
		}
		int charsetCode = (opcode & EmpireOpCodes.STRING_CHARSET_MASK) >> 4;
		EmpireCharset charset = EmpireCharsets.get(charsetCode);
		int length = opcode & EmpireOpCodes.STRING_LENGTH_MASK;
		if (length == 0) {
			length = in.readVLEInt();
		}
		char[] dest = new char[length];
		charset.decode(in, dest, 0, length);

		return MiscUtil.createString(dest);
	}

	@Override
	public BigInteger readBigInteger() {
		return new BigInteger(in.readBytes(in.readVLEInt()));
	}

	@Override
	public BigDecimal readBigDecimal() {
		BigInteger integer = readBigInteger();
		int scale = in.readVLEInt();
		return new BigDecimal(integer, scale);
	}

	@Override
	public Object[] readArray() {
		int length = in.readVLEInt();
		Object[] array = new Object[length];
		for (int i = 0; i < length; i++) {
			array[i] = readObject();
		}
		return array;
	}

	@Override
	public List<?> readList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<?> readSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<?, ?> readMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		in.close();
	}

}
