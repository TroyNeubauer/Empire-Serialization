package com.troy.test;

import java.io.*;

import com.troy.empireserialization.*;
import com.troy.empireserialization.io.in.*;
import com.troy.empireserialization.io.out.*;
import com.troy.empireserialization.util.*;
import com.troy.testframework.*;

import sun.misc.*;

public class Main {

	public static void main(String[] args) throws Throwable {
		/*File f = new File("./MyLibraryNative.dat");
		InputStreamInput in = new InputStreamInput(new FileInputStream(f));
		NativeMemoryBlock block = in.map(f.length());
		System.out.println(block);

		System.exit(0);

		final Unsafe unsafe = MiscUtil.getUnsafe();*/

		Library.doTest(Constants.BIG_HARRY_POTTER);
		System.exit(0);

		OutputSerializationStream stream = new OutputSerializationStream(new File("./test.dat"));
		stream.writeString(Constants.HARRY_POTTER);
		stream.close();

		// me - 51,550
		// utf8 - 70,262

	}

}
