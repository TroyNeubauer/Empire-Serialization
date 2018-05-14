package com.troy.junit;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import com.troy.empireserialization.EmpireInput;
import com.troy.empireserialization.EmpireOutput;

public class StringEncodingTest {
	
	private static final File file = new File("test.dat");
	
	@Test
	public void fourBitTest() {
		test(TestConstants.FOUR_BIT_STRING);
	}
	
	private static void test(String parameter) {
		EmpireOutput empOut = new EmpireOutput(file);
		empOut.writeString(parameter);
		empOut.close();
		
		EmpireInput empIn = new EmpireInput(file);
		String result = empIn.readString();
		assertEquals(parameter, result);
	}
}
