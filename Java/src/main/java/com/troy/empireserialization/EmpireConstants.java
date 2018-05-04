package com.troy.empireserialization;

public class EmpireConstants {

	public static final String HELLO_WORLD_STRING = "Hello, World!";

	//format:off
	public static final int NEXT_BYTE_VLE 		= 0b10000000;
	public static final int VLE_MASK 			= 0b01111111;
	
	public static final int USER_DEFINED_TYPE 	= 0b1 << 7;
	public static final int PRIMITIVE_TYPE 		= 0b0 << 7;
	//format:on

}
