
import java.io.*;
import java.util.*;

import com.esotericsoftware.kryo.*;
import com.esotericsoftware.kryo.io.*;
import com.troy.serialization.io.NativeOutput;
import com.troy.serialization.serializers.FieldSerializer;
import com.troy.serialization.util.InternalLog;
import com.troy.serialization.util.SerializationUtils;
import com.troy.serialization.util.StringFormatter;

public class Main {

	public static void main(String[] args) throws Throwable {

		ArrayList<String> test = new ArrayList<String>();
		test.add("FUCK GENERICS!");
		test.add("I REALLY HATE NO RUNTIME");
		test.add("TYPE");
		test.add("INFO");
		Kryo kryo = new Kryo();
		Output out = new Output(1024, Integer.MAX_VALUE);
		kryo.writeClassAndObject(out, test);
		FileOutputStream f = new FileOutputStream(new File("./test.dat"));
		f.write(Arrays.copyOf(out.getBuffer(), out.position()));
		f.close();
		System.out.println(new File("./test").getAbsolutePath());
		System.out.println('s');
		System.exit(0);
		new FieldSerializer<TestSub>(TestSub.class);

		SerializationUtils.init();
		NativeOutput o = new NativeOutput();
		o.writeInts(new int[] { 0x12, 0x324, 0x432, 0x123, 0x324 });
		System.out.println(StringFormatter.toHexString(o.toByteArray()));
		o.close();
		InternalLog.dumpToOut();
	}
}
