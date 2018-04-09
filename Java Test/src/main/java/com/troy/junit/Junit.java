package com.troy.junit;

import static org.junit.Assert.*;

import java.io.*;
import java.nio.*;

import org.junit.*;

import com.troy.empireserialization.exception.*;
import com.troy.empireserialization.io.out.*;

public class Junit {

	@Test(expected = NoBufferException.class)
	public void nativeOutNoBuffer() {
		NativeOutput out = new NativeOutput();
		out.getBuffer();
		out.close();
	}

	@Test(expected = NoBufferException.class)
	public void nativeFileOutNoBuffer() {
		NativeFileOutput out = new NativeFileOutput(new File("test.dat"));
		out.getBuffer();
		out.close();
	}

	@Test(expected = NoBufferException.class)
	public void nativeOutputStreamNoBuffer() {
		OutputStreamOutput out = new OutputStreamOutput(new ByteArrayOutputStream());
		out.getBuffer();
		out.close();
	}

	@Test
	public void byteArrayOutBigEndian() {
		byte[] expected = new byte[] {};
		ByteArrayOutput out = new ByteArrayOutput(20);
		out.setByteOrder(ByteOrder.BIG_ENDIAN);

		out.writeInt(5);// 0 - 4
		out.writeChar('a');// 4 - 1
		out.writeChar('C');// 5 - 1
		out.writeShort((short) 0x1C);// 6 - 2
		out.writeLong(0x0022446688AACCFFL);//8 - 8
		out.writeVLEInt(1);// 16 - 1
		out.writeVLEInt(5);// 16 - 1
		out.writeVLEInt(1);// 16 - 1

		byte[] result = out.getBuffer();
		assertArrayEquals(expected, result);
		out.close();
	}
}
