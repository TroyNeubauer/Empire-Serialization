package com.troy.junit;

import static org.junit.Assert.*;

import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;

import org.junit.*;

import com.troy.empireserialization.*;
import com.troy.empireserialization.charset.*;
import com.troy.empireserialization.exception.*;
import com.troy.empireserialization.io.out.*;
import com.troy.empireserialization.util.*;
import com.troy.test.*;

public class Junit {
	private static final File TEST_FILE = new File("JUint test.dat");

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
		byte[] expected = new byte[] { 0x00, 0x00, 0x00, 0x05, 0x00, 0x61, 0x00, 0x1c, 0x00, 0x22, 0x44, 0x66, (byte) 0x88, (byte) 0xaa, (byte) 0xcc,
				(byte) 0xff, 0x01, 0x05, (byte) 0x81, 0x7f };

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
		byte[] expected = new byte[] { 0x05, 0x00, 0x00, 0x00, 0x61, 0x00, 0x1c, 0x00, (byte) 0xff, (byte) 0xcc, (byte) 0xaa, (byte) 0x88, 0x66, 0x44,
				0x22, 0x00, 0x01, 0x05, (byte) 0x81, 0x7f };

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

	private static final byte[] MAP_TEST = new byte[] { 1, 5, 0, 9, 0, 45, 100, -24, 37, 5, 78, -12, 34, -56, -1, -5 };

	@Test
	public void byteArrayOutMap() {
		ByteArrayOutput out = new ByteArrayOutput(MAP_TEST.length);
		mapAndWrite(out, MAP_TEST);
		assertArrayEquals(out.getBuffer(), MAP_TEST);
		out.close();
	}

	@Test
	public void nativeFileOutMap() throws IOException {
		NativeFileOutput out = new NativeFileOutput(TEST_FILE);
		mapAndWrite(out, MAP_TEST);
		out.close();
		assertArrayEquals(Files.readAllBytes(Paths.get(TEST_FILE.getAbsolutePath())), MAP_TEST);
	}

	@Test
	public void nativeOutMap() throws IOException {
		NativeOutput out = new NativeOutput();
		mapAndWrite(out, MAP_TEST);

		byte[] temp = new byte[MAP_TEST.length];
		// Copy to temp
		NativeUtils.nativeToBytes(temp, out.address(), 0, MAP_TEST.length);
		assertArrayEquals(temp, MAP_TEST);
		out.close();
	}

	private void mapAndWrite(Output out, byte[] toWrite) {
		NativeMemoryBlock block = out.map(toWrite.length);

		NativeUtils.bytesToNative(block.address(), toWrite, 0, toWrite.length);
		block.setPosition(toWrite.length);

		out.unmap(block);
	}

	private static final char[] FOUR_BIT_TEST = "test".toCharArray();
	private static final char[] SIX_BIT_TEST = "This is Empire Serialization written by Troy Neubauer!".toCharArray();

	@Test
	public void nativeFourBit() {
		NativeUtils.NATIVES_ENABLED = true;
		ByteArrayOutput out = new ByteArrayOutput();
		EmpireCharsets.FOUR_BIT_CHARSET.encode(FOUR_BIT_TEST, out, 0, FOUR_BIT_TEST.length, StringInfo.ALL_ASCII);
		assertArrayEquals(Arrays.copyOf(out.getBuffer(), out.getBufferPosition()), new byte[] { 0b00010000, 0b01100001 });
	}

	@Test
	public void javaFourBit() {
		NativeUtils.NATIVES_ENABLED = false;
		ByteArrayOutput out = new ByteArrayOutput();
		EmpireCharsets.FOUR_BIT_CHARSET.encode(FOUR_BIT_TEST, out, 0, FOUR_BIT_TEST.length, StringInfo.ALL_ASCII);
		assertArrayEquals(Arrays.copyOf(out.getBuffer(), out.getBufferPosition()), new byte[] { 0b00010000, 0b01100001 });
	}

	@Test
	public void nativeSixBit() {
		NativeUtils.NATIVES_ENABLED = true;
		ByteArrayOutput out = new ByteArrayOutput();
		
		EmpireCharsets.SIX_BIT_CHARSET.encode(SIX_BIT_TEST, out, 0, SIX_BIT_TEST.length, StringInfo.ALL_ASCII);
		System.out.println(StringFormatter.toBinaryString(Arrays.copyOf(out.getBuffer(), out.getBufferPosition())));
		assertArrayEquals(Arrays.copyOf(out.getBuffer(), out.getBufferPosition()), new byte[] { 0b00010000, 0b01100001 });
	}

}
