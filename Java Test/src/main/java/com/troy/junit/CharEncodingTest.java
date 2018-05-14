package com.troy.junit;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import com.troy.empireserialization.charset.*;
import com.troy.empireserialization.io.out.*;
import com.troy.empireserialization.util.*;

public class CharEncodingTest {

	private static final char[] FOUR_BIT_TEST = TestConstants.FOUR_BIT_STRING.toCharArray();
	private static final byte[] FOUR_BIT_RESULT = new byte[] { 0b00010000, 0b01100001 };

	protected static final char[] SIX_BIT_TEST = TestConstants.SIX_BIT_STRING.toCharArray();
	private static final byte[] SIX_BIT_RESULT = new byte[] { -27, 53, 30, 1, 71, -128, -87, -122, -44, 117, 0, 56, 65, -43, 12, 93, 73, 76, 125, 70,
			-103, 2, 39, 84, 125, -12, 25, 0, -39, 0, -27, -42, -92, 3, 52, 32, 52, -56, 16, 7, 66 };

	private static final char[] VLE8_TEST = TestConstants.VLE8_STRING.toCharArray();

	@Test
	public void nativeFourBit() {
		NativeUtils.NATIVES_ENABLED = true;
		ByteArrayOutput out = new ByteArrayOutput();
		EmpireCharsets.FOUR_BIT_CHARSET.encode(FOUR_BIT_TEST, out, 0, FOUR_BIT_TEST.length, StringInfo.ALL_ASCII);
		assertArrayEquals(Arrays.copyOf(out.getBuffer(), out.getBufferPosition()), FOUR_BIT_RESULT);
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

		assertArrayEquals(Arrays.copyOf(out.getBuffer(), out.getBufferPosition()), SIX_BIT_RESULT);
	}

	@Test
	public void javaSixBit() {
		NativeUtils.NATIVES_ENABLED = false;
		ByteArrayOutput out = new ByteArrayOutput();

		EmpireCharsets.SIX_BIT_CHARSET.encode(SIX_BIT_TEST, out, 0, SIX_BIT_TEST.length, StringInfo.ALL_ASCII);

		assertArrayEquals(Arrays.copyOf(out.getBuffer(), out.getBufferPosition()), SIX_BIT_RESULT);
	}

}
