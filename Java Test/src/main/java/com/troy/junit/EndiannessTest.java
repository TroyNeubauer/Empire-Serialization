package com.troy.junit;

import static org.junit.Assert.*;

import java.nio.*;

import org.junit.*;

import com.troy.empireserialization.io.out.*;

public class EndiannessTest {

	@Test
	public void byteArrayOutBigEndian() {
		byte[] expected = new byte[] { 0x00, 0x00, 0x00, 0x05, 0x00, 0x61, 0x00, 0x1c, 0x00, 0x22, 0x44, 0x66,
				(byte) 0x88, (byte) 0xaa, (byte) 0xcc, (byte) 0xff, 0x01, 0x05, (byte) 0x81, 0x7f };

		ByteArrayOutput out = new ByteArrayOutput(20);
		out.setByteOrder(ByteOrder.BIG_ENDIAN);

		out.writeInt(5);// 0 - 4
		out.writeChar('a');// 4 - 2
		out.writeShort((short) 0x1C);// 6 - 2
		out.writeLong(0x0022446688AACCFFL);// 8 - 8
		out.writeVLEInt(1);// 16 - 1
		out.writeVLEInt(5);// 17 - 1
		out.writeVLEInt(0xFF);// 18 - 2

		byte[] result = out.getBuffer();
		assertArrayEquals(expected, result);
		out.close();
	}

	@Test
	public void byteArrayLittleBigEndian() {
		byte[] expected = new byte[] { 0x05, 0x00, 0x00, 0x00, 0x61, 0x00, 0x1c, 0x00, (byte) 0xff, (byte) 0xcc,
				(byte) 0xaa, (byte) 0x88, 0x66, 0x44, 0x22, 0x00, 0x01, 0x05, (byte) 0x81, 0x7f };

		ByteArrayOutput out = new ByteArrayOutput(20);
		out.setByteOrder(ByteOrder.LITTLE_ENDIAN);

		out.writeInt(5);// 0 - 4
		out.writeChar('a');// 4 - 2
		out.writeShort((short) 0x1C);// 6 - 2
		out.writeLong(0x0022446688AACCFFL);// 8 - 8
		out.writeVLEInt(1);// 16 - 1
		out.writeVLEInt(5);// 17 - 1
		out.writeVLEInt(0xFF);// 18 - 2

		byte[] result = out.getBuffer();
		assertArrayEquals(expected, result);
		out.close();
	}


}
