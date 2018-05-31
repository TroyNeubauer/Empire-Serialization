package com.troy.empireserialization;

import java.util.HashMap;

public class Version {
	public static final int VERSION_1 = 0b0000;
	
	public static final int CURRENT_VERSION = VERSION_1;

	private static final HashMap<Integer, String> VERSION_MAP = new HashMap<Integer, String>();

	static {
		VERSION_MAP.put(VERSION_1, "1");
		//Other versions will go here eventually
	}

	/**
	 * Returns a human readable version string from a version code
	 * 
	 * @param version
	 *            The version code
	 * @return A human readable string representing the version code Ie "1.2"
	 */
	public static String getVersionString(int version) {
		return VERSION_MAP.get(version);
	}
}
