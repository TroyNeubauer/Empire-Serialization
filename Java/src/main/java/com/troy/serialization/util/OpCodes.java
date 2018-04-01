package com.troy.serialization.util;

/**
 * 
 * See <a href="https://docs.google.com/document/d/1hk4uM8_i8ZAb3smQFcM6dKP8REH3ivORBcV8yDvftxo/edit?usp=sharing">https://docs.google.com</a>
 * 
 * @author Troy Neubauer
 */
public interface OpCodes {
	
	//format:off
	public static final int MAJOR_CODE_MASK 	= 0b11_000000;
	public static final int MINIOR_CODE_MASK 	= 0b00_111111;
	
	public static final int GENERAL_OPCODE_MAJOR_CODE 		= 0b00 << 6;
	public static final int STRING_TYPE_MAJOR_CODE 			= 0b01 << 6;
	public static final int UNSIGNED_INTEGER_MAJOR_CODE 	= 0b10 << 6;
	public static final int SIGNED_INTEGER_MAJOR_CODE 		= 0b11 << 6;
	
	//General minor codes
	public static final int NULL_REF_CONST 				= 0x00 & GENERAL_OPCODE_MAJOR_CODE;
	public static final int FLAGS_TYPE	 				= 0x01 & GENERAL_OPCODE_MAJOR_CODE;
	public static final int TYPE_DEF_OBJ_DEF_TYPE 		= 0x02 & GENERAL_OPCODE_MAJOR_CODE;
	public static final int TYPE_REF_OBJ_DEF_TYPE 		= 0x03 & GENERAL_OPCODE_MAJOR_CODE;
	public static final int OBJ_REF_TYPE 				= 0x04 & GENERAL_OPCODE_MAJOR_CODE;
	public static final int STRING_REF_TYPE 			= 0x05 & GENERAL_OPCODE_MAJOR_CODE;
	public static final int BOOLEAN_FALSE_CONST 		= 0x06 & GENERAL_OPCODE_MAJOR_CODE;
	public static final int BOOLEAN_TRUE_CONST 			= 0x07 & GENERAL_OPCODE_MAJOR_CODE;
	
	//8 Unassigned
	//9 Unassigned
	public static final int UNSIGNED_BYTE_TYPE 			= 0x0A & GENERAL_OPCODE_MAJOR_CODE;
	public static final int UNSIGNED_SHORT_TYPE 		= 0x0B & GENERAL_OPCODE_MAJOR_CODE;
	public static final int UNSIGNED_INT_TYPE			= 0x0C & GENERAL_OPCODE_MAJOR_CODE;
	public static final int UNSIGNED_LONG_TYPE			= 0x0D & GENERAL_OPCODE_MAJOR_CODE;
	public static final int UNSIGNED_lLONG_TYPE			= 0x0E & GENERAL_OPCODE_MAJOR_CODE;
	public static final int UNSIGNED_llLONG_TYPE		= 0x0F & GENERAL_OPCODE_MAJOR_CODE;
	
	public static final int SIGNED_BYTE_TYPE 			= 0x10 & GENERAL_OPCODE_MAJOR_CODE;
	public static final int SIGNED_SHORT_TYPE 			= 0x11 & GENERAL_OPCODE_MAJOR_CODE;
	public static final int SIGNED_INT_TYPE				= 0x12 & GENERAL_OPCODE_MAJOR_CODE;
	public static final int SIGNED_LONG_TYPE			= 0x13 & GENERAL_OPCODE_MAJOR_CODE;
	public static final int SIGNED_lLONG_TYPE			= 0x14 & GENERAL_OPCODE_MAJOR_CODE;
	public static final int SIGNED_llLONG_TYPE			= 0x15 & GENERAL_OPCODE_MAJOR_CODE;
	
	public static final int BIG_INTEGER_TYPE			= 0x16 & GENERAL_OPCODE_MAJOR_CODE;
	public static final int BIG_DECIMAL_TYPE			= 0x17 & GENERAL_OPCODE_MAJOR_CODE;
	//18 Unassigned
	public static final int HALF_TYPE					= 0x19 & GENERAL_OPCODE_MAJOR_CODE;
	public static final int FLOAT_TYPE					= 0x1A & GENERAL_OPCODE_MAJOR_CODE;
	public static final int DOUBLE_TYPE					= 0x1B & GENERAL_OPCODE_MAJOR_CODE;
	public static final int QUADRUPLE_TYPE				= 0x1C & GENERAL_OPCODE_MAJOR_CODE;
	public static final int OCTUPLE_TYPE				= 0x1D & GENERAL_OPCODE_MAJOR_CODE;
	
	public static final int FLOAT_0_CONST				= 0x1E & GENERAL_OPCODE_MAJOR_CODE;
	public static final int FLOAT_1_CONST				= 0x1F & GENERAL_OPCODE_MAJOR_CODE;
	public static final int FLOAT_2_CONST				= 0x20 & GENERAL_OPCODE_MAJOR_CODE;
	public static final int DOUBLE_0_CONST				= 0x21 & GENERAL_OPCODE_MAJOR_CODE;
	public static final int DOUBLE_1_CONST				= 0x22 & GENERAL_OPCODE_MAJOR_CODE;
	public static final int DOUBLE_2_CONST				= 0x23 & GENERAL_OPCODE_MAJOR_CODE;
	
	public static final int VLE_UNSIGNED_SHORT_TYPE 	= 0x24 & GENERAL_OPCODE_MAJOR_CODE;
	public static final int VLE_UNSIGNED_INT_TYPE 		= 0x25 & GENERAL_OPCODE_MAJOR_CODE;
	public static final int VLE_UNSIGNED_LONG_TYPE 		= 0x26 & GENERAL_OPCODE_MAJOR_CODE;
	
	public static final int VLE_SIGNED_SHORT_TYPE 		= 0x27 & GENERAL_OPCODE_MAJOR_CODE;
	public static final int VLE_SIGNED_INT_TYPE 		= 0x28 & GENERAL_OPCODE_MAJOR_CODE;
	public static final int VLE_SIGNED_LONG_TYPE 		= 0x29 & GENERAL_OPCODE_MAJOR_CODE;
	
	public static final int HELLO_WORLD_STRING_CONST 	= 0x30 & GENERAL_OPCODE_MAJOR_CODE;
	public static final int EMPTY_STRING_CONST		 	= 0x31 & GENERAL_OPCODE_MAJOR_CODE;
	
	//format:on
	
	
	
}
