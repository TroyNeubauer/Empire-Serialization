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
		return 0;
	}

	@Override
	public short readShort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int readInt() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long readLong() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte readUnsignedByte() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short readUnsignedShort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int readUnsignedInt() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long readUnsignedLong() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float readFloat() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double readDouble() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public char readChar() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean readBoolean() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String readString() {
		// TODO Auto-generated method stub
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
	public Object readArray() {
		int length = in.readVLEInt();
		return null;
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
