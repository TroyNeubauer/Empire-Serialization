package com.troy.serialization.util;

import java.io.File;
import java.nio.file.Path;

import com.troy.serialization.io.*;

public class Outputs {

	public Outputs() {
	}
	
	public static Output newFileOutput(Path path) {
		return newFileOutput(path.toAbsolutePath().toString());
	}
	
	public static Output newFileOutput(File file) {
		return newFileOutput(file.getPath());
	}
	
	public static Output newFileOutput(String file) {
		return new NativeFileOutput(file);
	}

}
