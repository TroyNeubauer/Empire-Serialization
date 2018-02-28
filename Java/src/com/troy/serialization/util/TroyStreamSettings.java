package com.troy.serialization.util;

public class TroyStreamSettings {
	private static final int VLE_MASK = 0b00000001, VLE_SHIFT = 0;
	public boolean useVariableLengthEncoding = true;

	public int getCompleteValue() {
		return (toInt(useVariableLengthEncoding) << VLE_SHIFT);
	}

	private static int toInt(boolean value) {
		return value ? 1 : 0;
	}
}
