package com.troy.serialization.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class InternalLog {

	private static final ByteArrayOutputStream streamImpl = new ByteArrayOutputStream(1024);
	private static final OutputStreamWriter stream = new OutputStreamWriter(streamImpl);
	
	private static final List<PrintStream> otherStreams = new ArrayList<PrintStream>();
	
	public static void addStream(PrintStream stream) {
		otherStreams.add(stream);
	}

	public static void dumpLogTo(PrintStream stream) {
		try {
			InternalLog.stream.flush();
			stream.write(streamImpl.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void clear() {
		streamImpl.reset();
	}
	
	public static void dumpToError() {
		dumpLogTo(System.err);
	}
	
	public static void dumpToOut() {
		dumpLogTo(System.out);
	}
	
	/**
	 * Writes a string to the internal log along with a newline
	 * 
	 * @param s
	 *            The string to write
	 */
	public static void log(String s) {
		try {
			stream.write(s);
			stream.write('\n');
			for(PrintStream stream : otherStreams) {
				stream.println(s);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Writes a string to the internal log without writing a newline
	 * 
	 * @param s
	 *            The string to write
	 */
	public static void print(String s) {
		try {
			stream.write(s);
			for(PrintStream stream : otherStreams) {
				stream.print(s);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Writes a string to the internal log along with a newline
	 * 
	 * @param s
	 *            The string to write
	 */
	public static void log(Object o) {
		log(String.valueOf(o));
	}

	/**
	 * Writes a string to the internal log without writing a newline
	 * 
	 * @param s
	 *            The string to write
	 */
	public static void print(Object s) {
		print(String.valueOf(s));
	}

}
