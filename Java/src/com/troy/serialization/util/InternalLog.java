package com.troy.serialization.util;

public class InternalLog {
	
	public static void log(String s) {
		
	}
	
	public static void log(Object o) {
		log(String.valueOf(o));
	}

}
