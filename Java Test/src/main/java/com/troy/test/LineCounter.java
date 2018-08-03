package com.troy.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LineCounter {
	static int files = 0;
	// Prints out the number of lines
	public static void main(String[] args) throws IOException {
		File file = new File("C:\\Empire Serialization");
		System.out.println(countLines(file));
		System.out.println("Files count: " + files);

		/*FileWriter writer = new FileWriter(new File("putFilesTogther.txt"));
		putFilesTogther(file, writer);
		writer.close();*/
	}

	public static long countLines(File file) {
		long total = 0;
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				if (child.isDirectory() || (child.getName().endsWith(".java") || child.getName().endsWith(".c"))) {
					if (child.isFile())
						System.out.println("reading " + child);
					total += countLines(child);
				}
			}
		} else {
			files++;
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				while (reader.readLine() != null)
					total++;
				reader.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return total;
	}

	public static void putFilesTogther(File file, FileWriter writer) {
		
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				if (child.isDirectory() || (child.getName().endsWith(".java") || child.getName().endsWith(".c"))) {
					putFilesTogther(child, writer);
				}
			}
		} else {//It's a file
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				writer.write("\n#File: " + file + "\n\n");
				String line;
				while ((line = reader.readLine()) != null) {
					writer.write(line);
					writer.write('\n');
				}
				reader.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
