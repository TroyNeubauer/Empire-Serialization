package com.troy.empireserialization;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.troy.empireserialization.io.in.Input;

public class EmpireInput implements ObjectIn {

	private Input in;

	public EmpireInput(Input in) {
		this.in = in;
	}

	@Override
	public <T> T readObject() {
		// TODO Auto-generated method stub
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
		return null;
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
		for(int i = 0; i < length; i++) {
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
