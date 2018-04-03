package com.troy.test;

import java.io.*;

import com.troy.serialization.*;
import com.troy.serialization.io.*;

public class Main {

	public static void main(String[] args) throws Throwable {
		
		NativeFileOutput out = new NativeFileOutput(new File("./test.dat"));
		NativeMemoryBlock block = out.map(100);
		System.out.println(block);
		
		System.exit(0);
		OutputSerializationStream stream = new OutputSerializationStream(new File("./test.dat"));
		stream.writeString(Constants.HARRY_POTTER);
		stream.close();
		
		//me - 51,550
		//utf8 - 70,262

	}
}
