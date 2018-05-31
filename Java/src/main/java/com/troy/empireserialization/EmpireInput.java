package com.troy.empireserialization;

import static com.troy.empireserialization.EmpireConstants.HELLO_WORLD_STRING;
import static com.troy.empireserialization.EmpireOpCodes.*;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.troy.empireserialization.cache.IntKeyCache;
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
	private IntKeyCache<Class<?>> classCache = new IntKeyCache<Class<?>>(100, 1.0);
	private IntKeyCache<Object> objectCache = new IntKeyCache<Object>(200, 1.0);
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
	public Object readObject() {
		return readObjectImpl();
	}
	
	@Override
	public Object readObjectRecursive() {
		return readObjectImpl();
	}
	
	private Object readObjectImpl() {
		int opcode = in.readByte();
		int majorOpcode = opcode & MAJOR_CODE_MASK;
		int minorOpcode = opcode & MINIOR_CODE_MASK;
		if (majorOpcode == GENERAL_OPCODE_MAJOR_CODE) {
			if (opcode == TYPE_REF_OBJ_DEF_TYPE) {
			} else if (opcode == OBJ_REF_TYPE) {
				return objectCache.get(in.readVLEInt()).value;
			} else if (opcode == TYPE_DEF_OBJ_DEF_TYPE) {

			} else if (opcode == NULL_REF_CONST) {
				return null;
			} else if (opcode == STRING_REF_TYPE) {
				return stringCache.get(in.readVLEInt());
			} else if (opcode == EMPTY_STRING_CONST) {
				return "";// Hope that its not a data structure
			} else if (opcode == HELLO_WORLD_STRING_CONST) {
				return HELLO_WORLD_STRING;
			}
		} else if (majorOpcode == DATA_STRUCTURES_MAJOR_CODE) {
			int opcodeOver4 = minorOpcode / 4;
			if (opcodeOver4 / 4 == PRIMITIVE_ARRAY_TYPE / 4) {
				return readArrayImpl(opcode);
			} else if (opcodeOver4 / 4 == PRIMITIVE_MAP_TYPE / 4) {
				return readMapImpl(opcode);
			}
		} else if (majorOpcode == NUMBER_MAJOR_CODE) {
			return Integer.valueOf(minorOpcode);
		} else if (majorOpcode == STRING_MAJOR_CODE) {
			return readStringImpl(opcode);
		} else {// There should be no other major codes unless something is very wrong
			throw new Error();
		}
		return null;
	}

	private Object[] readArrayImpl(int opcode) {
		if (opcode == PRIMITIVE_ARRAY_TYPE) {

		} else if (opcode == USER_DEFINED_TYPE_DEF_ARRAY_TYPE) {

		} else if (opcode == USER_DEFINED_TYPE_REF_ARRAY_TYPE) {

		} else if (opcode == POLYMORPHIC_ARRAY_TYPE) {

		}
		return null;
	}

	private Map<?, ?> readMapImpl(int opcode) {
		if (opcode == PRIMITIVE_MAP_TYPE) {

		} else if (opcode == USER_DEFINED_TYPE_DEF_MAP_TYPE) {

		} else if (opcode == USER_DEFINED_TYPE_REF_MAP_TYPE) {

		} else if (opcode == POLYMORPHIC_MAP_TYPE) {

		}
		return null;
	}
	
	public String readString() {
		return readStringImpl(in.readByte());
	}

	private String readStringImpl(int opcode) {
		if ((opcode & MAJOR_CODE_MASK) != STRING_MAJOR_CODE) {
			throw new MismatchedInputException("Expected a String major opcode " + StringFormatter.toBinaryString(EmpireOpCodes.STRING_MAJOR_CODE)
					+ " but instead read " + StringFormatter.toBinaryString(opcode & MAJOR_CODE_MASK));
		}
		int charsetCode = (opcode & STRING_CHARSET_MASK) >> 4;
		EmpireCharset charset = EmpireCharsets.get(charsetCode);
		int length = opcode & STRING_LENGTH_MASK;
		if (length == 0) {
			length = in.readVLEInt();
		}
		char[] dest = new char[length];
		charset.decode(in, dest, 0, length);

		return MiscUtil.createString(dest);
	}
/*
	@Override
	public BigInteger readBigInteger() {
		return new BigInteger(in.readBytes(in.readVLEInt()));
	}

	@Override
	public BigDecimal readBigDecimal() {
		BigInteger integer = readBigInteger();
		int scale = in.readVLEInt();
		return new BigDecimal(integer, scale);
	}*/

	@Override
	public void close() {
		in.close();
	}

}
