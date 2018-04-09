package com.troy.junit;

import static org.junit.Assert.*;

import java.io.*;
import java.nio.file.*;

import org.junit.*;

import com.troy.empireserialization.*;
import com.troy.empireserialization.io.out.*;
import com.troy.empireserialization.util.*;

import static com.troy.junit.JUnitConstants.*;

public class NativeMapTest {
	
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
}
