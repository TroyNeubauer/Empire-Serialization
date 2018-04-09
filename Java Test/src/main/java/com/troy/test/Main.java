package com.troy.test;

import java.io.*;

import com.troy.empireserialization.*;
import com.troy.testframework.*;

public class Main {

	public static void main(String[] args) throws Throwable {
	
		
		/*File f = new File("./MyLibraryNative.dat");
		InputStreamInput in = new InputStreamInput(new FileInputStream(f));
		NativeMemoryBlock block = in.map(f.length());
		System.out.println(block);

		System.exit(0);

		final Unsafe unsafe = MiscUtil.getUnsafe();*/

		Library.doTest(Constants.BIG_HARRY_POTTER);

	}

}
