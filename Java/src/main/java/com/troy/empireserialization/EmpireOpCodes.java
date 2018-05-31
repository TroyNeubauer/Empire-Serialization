package com.troy.empireserialization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.troy.empireserialization.util.ClassHelper;

/**
 * 
 * See <a href=
 * "https://docs.google.com/document/d/1hk4uM8_i8ZAb3smQFcM6dKP8REH3ivORBcV8yDvftxo/edit?usp=sharing">https://docs.google.com</a>
 * 
 * @author Troy Neubauer
 */
public class EmpireOpCodes {

	//format:off
	public static final int MAJOR_CODE_MASK 					= 0b11 << 6;
	public static final int MINIOR_CODE_MASK 					= 0b00111111;
	
	public static final int GENERAL_OPCODE_MAJOR_CODE 			= 0b00 << 6;
	public static final int STRING_MAJOR_CODE 					= 0b01 << 6;
	public static final int DATA_STRUCTURES_MAJOR_CODE 			= 0b10 << 6;
	public static final int NUMBER_MAJOR_CODE 					= 0b11 << 6;
	
	public static final int STRING_CHARSET_MASK					= 0b00110000;
	public static final int STRING_LENGTH_MASK					= 0b00001111;
	
	//General minor codes
	public static final int NULL_REF_CONST 						= 0x00 | GENERAL_OPCODE_MAJOR_CODE;
	public static final int FLAGS_TYPE	 						= 0x01 | GENERAL_OPCODE_MAJOR_CODE;
	public static final int TYPE_DEF_OBJ_DEF_TYPE 				= 0x02 | GENERAL_OPCODE_MAJOR_CODE;
	public static final int TYPE_REF_OBJ_DEF_TYPE 				= 0x03 | GENERAL_OPCODE_MAJOR_CODE;
	public static final int OBJ_REF_TYPE 						= 0x04 | GENERAL_OPCODE_MAJOR_CODE;
	public static final int STRING_REF_TYPE 					= 0x05 | GENERAL_OPCODE_MAJOR_CODE;
	public static final int BOOLEAN_FALSE_CONST 				= 0x06 | GENERAL_OPCODE_MAJOR_CODE;
	public static final int BOOLEAN_TRUE_CONST 					= 0x07 | GENERAL_OPCODE_MAJOR_CODE;
	
	public static final int STRING_TYPE 						= 0x08 | GENERAL_OPCODE_MAJOR_CODE;
	//9 Unassigned
	public static final int UNSIGNED_BYTE_TYPE 					= 0x0A | GENERAL_OPCODE_MAJOR_CODE;
	public static final int UNSIGNED_SHORT_TYPE 				= 0x0B | GENERAL_OPCODE_MAJOR_CODE;
	public static final int UNSIGNED_INT_TYPE					= 0x0C | GENERAL_OPCODE_MAJOR_CODE;
	public static final int UNSIGNED_LONG_TYPE					= 0x0D | GENERAL_OPCODE_MAJOR_CODE;
	public static final int UNSIGNED_lLONG_TYPE					= 0x0E | GENERAL_OPCODE_MAJOR_CODE;
	public static final int UNSIGNED_llLONG_TYPE				= 0x0F | GENERAL_OPCODE_MAJOR_CODE;
	
	public static final int SIGNED_BYTE_TYPE 					= 0x10 | GENERAL_OPCODE_MAJOR_CODE;
	public static final int SIGNED_SHORT_TYPE 					= 0x11 | GENERAL_OPCODE_MAJOR_CODE;
	public static final int SIGNED_INT_TYPE						= 0x12 | GENERAL_OPCODE_MAJOR_CODE;
	public static final int SIGNED_LONG_TYPE					= 0x13 | GENERAL_OPCODE_MAJOR_CODE;
	public static final int SIGNED_lLONG_TYPE					= 0x14 | GENERAL_OPCODE_MAJOR_CODE;
	public static final int SIGNED_llLONG_TYPE					= 0x15 | GENERAL_OPCODE_MAJOR_CODE;
	
	public static final int BIG_INTEGER_TYPE					= 0x16 | GENERAL_OPCODE_MAJOR_CODE;
	public static final int BIG_DECIMAL_TYPE					= 0x17 | GENERAL_OPCODE_MAJOR_CODE;
	//18 Unassigned
	public static final int HALF_TYPE							= 0x19 | GENERAL_OPCODE_MAJOR_CODE;
	public static final int FLOAT_TYPE							= 0x1A | GENERAL_OPCODE_MAJOR_CODE;
	public static final int DOUBLE_TYPE							= 0x1B | GENERAL_OPCODE_MAJOR_CODE;
	public static final int QUADRUPLE_TYPE						= 0x1C | GENERAL_OPCODE_MAJOR_CODE;
	public static final int OCTUPLE_TYPE						= 0x1D | GENERAL_OPCODE_MAJOR_CODE;
	
	public static final int HELLO_WORLD_STRING_CONST 			= 0x1E | GENERAL_OPCODE_MAJOR_CODE;
	public static final int EMPTY_STRING_CONST		 			= 0x1F | GENERAL_OPCODE_MAJOR_CODE;
	
	public static final int VLE_UNSIGNED_SHORT_TYPE 			= 0x20 | GENERAL_OPCODE_MAJOR_CODE;
	public static final int VLE_UNSIGNED_INT_TYPE 				= 0x21 | GENERAL_OPCODE_MAJOR_CODE;
	public static final int VLE_UNSIGNED_LONG_TYPE 				= 0x22 | GENERAL_OPCODE_MAJOR_CODE;
	
	public static final int VLE_SIGNED_SHORT_TYPE 				= 0x23 | GENERAL_OPCODE_MAJOR_CODE;
	public static final int VLE_SIGNED_INT_TYPE 				= 0x24 | GENERAL_OPCODE_MAJOR_CODE;
	public static final int VLE_SIGNED_LONG_TYPE 				= 0x25 | GENERAL_OPCODE_MAJOR_CODE;
	
	//Data Structures
	
	public static final int PRIMITIVE_ARRAY_TYPE		 		= 0x00 | DATA_STRUCTURES_MAJOR_CODE;
	public static final int USER_DEFINED_TYPE_DEF_ARRAY_TYPE	= 0x01 | DATA_STRUCTURES_MAJOR_CODE;
	public static final int USER_DEFINED_TYPE_REF_ARRAY_TYPE	= 0x02 | DATA_STRUCTURES_MAJOR_CODE;
	public static final int POLYMORPHIC_ARRAY_TYPE				= 0x03 | DATA_STRUCTURES_MAJOR_CODE;
	
	public static final int PRIMITIVE_MAP_TYPE		 			= 0x08 | DATA_STRUCTURES_MAJOR_CODE;
	public static final int USER_DEFINED_TYPE_DEF_MAP_TYPE		= 0x09 | DATA_STRUCTURES_MAJOR_CODE;
	public static final int USER_DEFINED_TYPE_REF_MAP_TYPE		= 0x0A | DATA_STRUCTURES_MAJOR_CODE;
	public static final int POLYMORPHIC_MAP_TYPE				= 0x0B | DATA_STRUCTURES_MAJOR_CODE;
	
	
	
	//format:on
	public static final HashMap<Class<?>, Integer> PRIMITIVE_TYPE_MAPPING;
	public static final HashMap<Class<?>, Integer> PRIMITIVE_TYPE_VLE_MAPPING;

	static {
		PRIMITIVE_TYPE_MAPPING = new HashMap<Class<?>, Integer>();
		PRIMITIVE_TYPE_MAPPING.put(boolean.class, UNSIGNED_BYTE_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(byte.class, SIGNED_BYTE_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(short.class, SIGNED_SHORT_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(int.class, SIGNED_INT_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(long.class, SIGNED_LONG_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(float.class, FLOAT_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(double.class, DOUBLE_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(char.class, UNSIGNED_SHORT_TYPE);

		PRIMITIVE_TYPE_MAPPING.put(Boolean.class, UNSIGNED_BYTE_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(Byte.class, SIGNED_BYTE_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(Short.class, SIGNED_SHORT_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(Integer.class, SIGNED_INT_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(Long.class, SIGNED_LONG_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(Float.class, FLOAT_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(Double.class, DOUBLE_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(Character.class, SIGNED_SHORT_TYPE);

		PRIMITIVE_TYPE_MAPPING.put(boolean[].class, PRIMITIVE_ARRAY_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(byte[].class, PRIMITIVE_ARRAY_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(short[].class, PRIMITIVE_ARRAY_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(int[].class, PRIMITIVE_ARRAY_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(long[].class, PRIMITIVE_ARRAY_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(float[].class, PRIMITIVE_ARRAY_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(double[].class, PRIMITIVE_ARRAY_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(char[].class, PRIMITIVE_ARRAY_TYPE);

		PRIMITIVE_TYPE_MAPPING.put(Boolean[].class, PRIMITIVE_ARRAY_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(Byte[].class, PRIMITIVE_ARRAY_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(Short[].class, PRIMITIVE_ARRAY_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(Integer[].class, PRIMITIVE_ARRAY_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(Long[].class, PRIMITIVE_ARRAY_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(Float[].class, PRIMITIVE_ARRAY_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(Double[].class, PRIMITIVE_ARRAY_TYPE);
		PRIMITIVE_TYPE_MAPPING.put(Character[].class, PRIMITIVE_ARRAY_TYPE);

		PRIMITIVE_TYPE_MAPPING.put(String.class, STRING_TYPE);

		PRIMITIVE_TYPE_VLE_MAPPING = new HashMap<Class<?>, Integer>(PRIMITIVE_TYPE_MAPPING);

		PRIMITIVE_TYPE_VLE_MAPPING.put(short.class, VLE_SIGNED_SHORT_TYPE);
		PRIMITIVE_TYPE_VLE_MAPPING.put(int.class, VLE_SIGNED_INT_TYPE);
		PRIMITIVE_TYPE_VLE_MAPPING.put(long.class, VLE_SIGNED_LONG_TYPE);
		PRIMITIVE_TYPE_VLE_MAPPING.put(Short.class, VLE_SIGNED_SHORT_TYPE);
		PRIMITIVE_TYPE_VLE_MAPPING.put(Integer.class, VLE_SIGNED_INT_TYPE);
		PRIMITIVE_TYPE_VLE_MAPPING.put(Long.class, VLE_SIGNED_LONG_TYPE);

	}

}
