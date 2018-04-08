package com.troy.serialization.util;

import com.troy.serialization.charset.*;

public class StringInfo {
	public static final int ALL_ASCII = 0;
	public static final int OTHER = 1;
	
	public EmpireCharset charset;
	public int info;
	
	public StringInfo(int info) {
		this.info = info;
	}
	
	public StringInfo(EmpireCharset charset, int info) {
		this.charset = charset;
		this.info = info;
	}	
}
