package com.troy.junit;

import java.io.*;

import org.junit.*;

import com.troy.empireserialization.exception.*;
import com.troy.empireserialization.io.out.*;

public class NoBufferExceptionTest {

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

}
