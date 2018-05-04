package com.troy.empireserialization;

public class SerializationSettings {
	public static final SerializationSettings defaultSettings = new SerializationSettings();
	
	static {
		defaultSettings.inferFinalClasses = true;
	}

	public boolean inferFinalClasses;
	public boolean useVLE;
	public boolean useLittleEndian;
	public int version = Version.VERSION;

	public SerializationSettings() {

	}

	
	public static SerializationSettings getDefaultSettings() {
		return defaultSettings;
	}


	public int getFlagsData(boolean pluginsPresent) {
		
		return 0;
	}

}
