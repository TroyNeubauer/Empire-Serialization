package test;

import java.io.*;
import java.util.Arrays;

import com.troy.serialization.charset.TroyCharsets;
import com.troy.serialization.io.ByteArrayOutput;
import com.troy.serialization.util.SerializationUtils;
import com.troyberry.util.StringFormatter;

public class Main {

	public static void main(String[] args)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException, FileNotFoundException, IOException {

		char[] src = new char[] { 'a', 'a', 'a', 'a' };

		SerializationUtils.init();
		ByteArrayOutput out = new ByteArrayOutput();
		TroyCharsets.FOUR_BIT_ENCODING.encode(src, out, 0, src.length, true);
		byte[] buf = Arrays.copyOf(out.getBuffer(), out.getBufferPosition());
		new FileOutputStream(new File("test.dat")).write(buf);
		System.out.println(StringFormatter.toBinaryString(buf));

		/*
		 * char[] src = new char[] { 'T', 'r', 'o', 'y' , '!'};
		 * 
		 * 
		 * SerializationUtils.init(); ByteArrayOutput out = new ByteArrayOutput(); TroyCharsets.SIX_BIT_ENCODING.encode(src, out, 0, src.length, true);
		 * byte[] buf = Arrays.copyOf(out.getBuffer(), out.getBufferPosition()); new FileOutputStream(new File("test.dat")).write(buf);
		 * System.out.println(StringFormatter.toBinaryString(buf));
		 */
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
