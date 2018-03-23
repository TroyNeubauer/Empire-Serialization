package com.troy.serialization.util;

public interface OpCodes {
	
	//format:off
	public static final int NULL_REF_CONST 				= 0x00;
	public static final int FLAGS_TYPE	 				= 0x01;
	public static final int OBJ_DEF_TYPE_DEF_TYPE 		= 0x02;
	public static final int OBJ_DEF_TYPE_REF_TYPE 		= 0x03;
	public static final int OBJ_REF_TYPE 				= 0x04;
	public static final int STRING_REF_TYPE 			= 0x05;
	public static final int BOOLEAN_FALSE_CONST 		= 0x06;
	public static final int BOOLEAN_TRUE_CONST 			= 0x07;
	
	//8 Unassigned
	//9 Unassigned
	public static final int UNSIGNED_BYTE_TYPE 			= 0x0A;
	public static final int UNSIGNED_SHORT_TYPE 		= 0x0B;
	public static final int UNSIGNED_INT_TYPE			= 0x0C;
	public static final int UNSIGNED_LONG_TYPE			= 0x0D;
	public static final int UNSIGNED_lLONG_TYPE			= 0x0E;
	public static final int UNSIGNED_llLONG_TYPE		= 0x0F;
	
	public static final int SIGNED_BYTE_TYPE 			= 0x10;
	public static final int SIGNED_SHORT_TYPE 			= 0x11;
	public static final int SIGNED_INT_TYPE				= 0x12;
	public static final int SIGNED_LONG_TYPE			= 0x13;
	public static final int SIGNED_lLONG_TYPE			= 0x14;
	public static final int SIGNED_llLONG_TYPE			= 0x15;
	
	public static final int BIG_INTEGER_TYPE			= 0x16;
	//17 Unassigned
	//18 Unassigned
	public static final int HALF_TYPE					= 0x19;
	public static final int FLOAT_TYPE					= 0x1A;
	public static final int DOUBLE_TYPE					= 0x1B;
	public static final int QUADRUPLE_TYPE				= 0x1C;
	public static final int OCTUPLE_TYPE				= 0x1D;
	
	public static final int FLOAT_0_CONST				= 0x1E;
	public static final int FLOAT_1_CONST				= 0x1F;
	public static final int FLOAT_2_CONST				= 0x20;
	public static final int DOUBLE_0_CONST				= 0x21;
	public static final int DOUBLE_1_CONST				= 0x22;
	public static final int DOUBLE_2_CONST				= 0x23;
	
	public static final int VLE_UNSIGNED_SHORT_TYPE 	= 0x24;
	public static final int VLE_UNSIGNED_INT_TYPE 		= 0x25;
	public static final int VLE_UNSIGNED_LONG_TYPE 		= 0x26;
	
	public static final int VLE_SIGNED_SHORT_TYPE 		= 0x27;
	public static final int VLE_SIGNED_INT_TYPE 		= 0x28;
	public static final int VLE_SIGNED_LONG_TYPE 		= 0x29;
	
	public static final int HELLO_WORLD_STRING_CONST 	= 0x32;//Represents the familiar string "Hello World!"
	//format:on
	
	
	
}
