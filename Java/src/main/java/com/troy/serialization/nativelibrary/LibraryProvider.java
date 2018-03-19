package com.troy.serialization.nativelibrary;

import java.util.*;

import com.troy.serialization.nativelibrary.NativeLibrary.NativeLibraryNameResolver;
import com.troy.serialization.util.InternalLog;
import com.troy.serialization.util.MiscUtil;

public abstract class LibraryProvider {

	private static final List<LibraryProvider> providers = new ArrayList<LibraryProvider>();
	protected static final List<NativeLibrary> libs = new ArrayList<NativeLibrary>();

	// Set from native code to indicate that the native library was loaded
	// successfully
	private static boolean native_load = false;

	private static final Object LOAD_LOCK = new Object();

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
					boolean loaded;
					synchronized (LOAD_LOCK) {
						InternalLog.log("Attempting to load native library: " + lib);
						provider.load(lib);
						InternalLog.log("\tLibrary: " + lib + " is now loaded with the VM");
						loaded = native_load;// Copy native_load so we can reset it later and preserve its value now
						InternalLog.log("\tNative library: " + lib + " " + (loaded ? "correctly set the boolean indicating it was loaded properly"
								: "failed to set the boolean indicating it was loaded properly"));
						native_load = false;
					}
					return loaded;
				} catch (Exception e) {
					InternalLog.log("\tFailed to load native library: " + lib + "\n" + MiscUtil.getStackTrace(e));
				}

			}
		}
		return false;
	}

	/**
	 * Attempts to load the requested native library, throwing an exception if any
	 * error occurs, otherwise returning normally
	 * 
	 * @param lib
	 *            The native library to load
	 * @throws Exception
	 *             If any issue is detected that would prevent the desired native
	 *             library from being loaded and linked successfully
	 */
	public abstract void load(NativeLibrary lib) throws Exception;
}
