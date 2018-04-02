package com.troy.test;

import java.io.*;

import com.troy.serialization.*;

public class Main {

	public static void main(String[] args) throws Throwable {

		OutputSerializationStream stream = new OutputSerializationStream(new File("./test.dat"));
		stream.writeString(Constants.HARRY_POTTER);
		stream.close();
		
		//me - 51,550
		//utf8 - 70,262

	}
}
