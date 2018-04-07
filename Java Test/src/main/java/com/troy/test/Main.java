package com.troy.test;

import java.io.*;

import com.troy.serialization.*;
import com.troy.serialization.util.*;
import com.troy.testframework.*;

import sun.misc.*;

public class Main {

	public static void main(String[] args) throws Throwable {
		final Unsafe unsafe = MiscUtil.getUnsafe();

		Library.doTest(Constants.VLE8_TEST);
		System.exit(0);

		
		OutputSerializationStream stream = new OutputSerializationStream(new File("./test.dat"));
		stream.writeString(Constants.HARRY_POTTER);
		stream.close();

		// me - 51,550
		// utf8 - 70,262

	}

}
