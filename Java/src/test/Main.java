package test;

import java.io.*;
import java.nio.charset.Charset;

import com.troy.serialization.charset.TroyCharsets;
import com.troy.serialization.util.SerializationUtils;
import com.troyberry.util.StringFormatter;

public class Main {

	public static void main(String[] args)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException, FileNotFoundException, IOException {
		SerializationUtils.init();
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(new File("unicode test.txt")), Charset.forName("UTF-8"));
		writer.write(0x1F957);
		writer.close();
		
		char[] src = "eat".toCharArray();
		byte[] dest = new byte[(src.length + 3) / 2 - 1];
		TroyCharsets.FOUR_BIT_ENCODING.encode(src, dest, 0, 0, src.length, SerializationUtils.CHECK_CHARSET_PROBLEMS);
		System.out.println(StringFormatter.toHexString(dest));

	}

	/*
	 * String s = "tttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt"; for(int i = 0; i < 21;
	 * i++) { s += s; } char[] source = s.toCharArray();
	 * 
	 * byte[] dest = new byte[(source.length + 3) / 2 - 1]; long start;
	 * 
	 * start = System.nanoTime(); Output out = new Output(new FileOutputStream(new File("kryo.dat"))); out.writeString(s); out.close();
	 * System.out.println("kryo " + ((System.nanoTime() - start) / 1000000.0));
	 * 
	 * start = System.nanoTime(); TroyCharsets.FOUR_BIT_ENCODING.encode(source, dest, 0, 0, source.length,
	 * SerializationUtils.CHECK_CHARSET_PROBLEMS); FileOutputStream stream = new FileOutputStream(new File("test.dat")); stream.write(dest);
	 * stream.close(); System.out.println("my time " + ((System.nanoTime() - start) / 1000000.0));
	 */

}
