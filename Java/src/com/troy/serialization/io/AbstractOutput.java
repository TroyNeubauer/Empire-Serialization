package com.troy.serialization.io;

import com.troy.serialization.util.TroyStreamSettings;

public abstract class AbstractOutput implements Output {
	private TroyStreamSettings settings;

	@Override
	public void writeShort(short b) {
		if (settings.useVariableLengthEncoding) {

		} else {

		}
	}

	@Override
	public void writeInt(int b) {
		if (settings.useVariableLengthEncoding) {

		} else {

		}
	}

	@Override
	public void writeLong(long b) {
		if (settings.useVariableLengthEncoding) {

		} else {

		}
	}

	@Override
	public void writeFloat(float b) {
		if (settings.useVariableLengthEncoding) {

		} else {

		}
	}

	@Override
	public void writeDouble(double b) {
		if (settings.useVariableLengthEncoding) {

		} else {

		}
	}

	@Override
	public void writeChar(char b) {
		if (settings.useVariableLengthEncoding) {

		} else {

		}
	}

	@Override
	public void writeBoolean(boolean b) {

	}

	public void setSettings(TroyStreamSettings settings) {
		this.settings = settings;
	}

	public TroyStreamSettings getSettings() {
		return settings;
	}

}
