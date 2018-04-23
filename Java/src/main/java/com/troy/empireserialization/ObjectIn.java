package com.troy.empireserialization;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ObjectIn extends AutoCloseable {
	
	public <T> T readObject();

	// For writing "primitives"

	public byte readByte();

	public short readShort();

	public int readInt();

	public long readLong();

	public float readFloat();

	public double readDouble();

	public char readChar();

	public boolean readBoolean();

	public String readString();

	public BigInteger readBigInteger();

	public BigDecimal readBigDecimal();

	public Object[] readArray();

	public List<?> readList();

	public Set<?> readSet();

	public Map<?, ?> readMap();

	public void close();
}
