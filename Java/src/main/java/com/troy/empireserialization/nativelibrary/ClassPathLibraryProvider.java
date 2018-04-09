package com.troy.empireserialization.nativelibrary;

public class ClassPathLibraryProvider extends LibraryProvider {

	@Override
	public void load(NativeLibrary lib) throws Exception {
		System.loadLibrary(lib.getPlatformIndependentName());
	}
}
