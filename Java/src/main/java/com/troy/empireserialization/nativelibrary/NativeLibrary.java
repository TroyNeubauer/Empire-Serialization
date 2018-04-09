package com.troy.empireserialization.nativelibrary;

import java.util.Objects;

import com.troy.empireserialization.util.*;

public class NativeLibrary {
	private String baseName;
	private NativeLibraryNameResolver resolver;

	public NativeLibrary(String baseName, NativeLibraryNameResolver resolver) {
		this.baseName = Objects.requireNonNull(baseName);
		this.resolver = Objects.requireNonNull(resolver);
	}

	public String getNativeName() {
		return System.mapLibraryName(getPlatformIndependentName());
	}

	public String getPlatformIndependentName() {
		return resolver.getPlatformIndependentName(baseName, NativeUtils.IS_64_BIT);
	}

	public interface NativeLibraryNameResolver {
		/**
		 * Returns the name of the native library stripped of any platform specific prefix using the info provided
		 * 
		 * @param baseName The name of library as passed to the constructor
		 * @param _64Bit
		 * @return
		 */
		public String getPlatformIndependentName(String baseName, boolean _64Bit);

	}
}
