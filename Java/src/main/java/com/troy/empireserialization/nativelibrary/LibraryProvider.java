package com.troy.empireserialization.nativelibrary;

import java.util.*;

import com.troy.empireserialization.nativelibrary.NativeLibrary.*;
import com.troy.empireserialization.util.*;

public abstract class LibraryProvider {

	private static final List<LibraryProvider> providers = new ArrayList<LibraryProvider>();
	protected static final List<NativeLibrary> libs = new ArrayList<NativeLibrary>();

	private static final Object LOAD_LOCK = new Object();

	static {
		providers.add(new ClassPathLibraryProvider());
		libs.add(new NativeLibrary("TS", new NativeLibraryNameResolver() {
			@Override
			public String getPlatformIndependentName(String baseName, boolean _64Bit) {
				return baseName + (_64Bit ? "64" : "32");
			}

		}) {//Override toString
			@Override
			public String toString() {
				return "TS Native Library";
			}
		});
	}

	public static boolean loadLibrary() {
		for (LibraryProvider provider : providers) {
			for (NativeLibrary lib : libs) {
				try {
					synchronized (LOAD_LOCK) {
						InternalLog.log("Attempting to load native library: " + lib);
						provider.load(lib);
						InternalLog.log("\tLibrary: " + lib + " is now loaded with the VM");
					}
					return true;
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
