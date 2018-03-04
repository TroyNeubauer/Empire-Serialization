package com.troy.serialization.nativelibrary;

import java.util.*;

import com.troy.serialization.nativelibrary.NativeLibrary.NativeLibraryNameResolver;

public abstract class LibraryProvider {

	private static final List<LibraryProvider> providers = new ArrayList<LibraryProvider>();
	protected static final List<NativeLibrary> libs = new ArrayList<NativeLibrary>();

	// Set from native code to indicate that the native library was loaded successfully
	private static boolean native_load = false;

	static {
		providers.add(new ClassPathLibraryProvider());
		libs.add(new NativeLibrary("TS", new NativeLibraryNameResolver() {

			@Override
			public String getPlatformIndependentName(String baseName, boolean _64Bit) {
				return baseName + (_64Bit ? "64" : "32");
			}
		}));
	}

	public static boolean loadLibrary() {
		for (LibraryProvider provider : providers) {
			for (NativeLibrary lib : libs) {
				try {
					provider.load(lib);
					return isLibraryLoaded();
				} catch (Exception e) {
				}
			}
		}
		return false;
	}

	private static boolean isLibraryLoaded() {
		return native_load;
	}

	public abstract void load(NativeLibrary lib) throws Exception;
}
