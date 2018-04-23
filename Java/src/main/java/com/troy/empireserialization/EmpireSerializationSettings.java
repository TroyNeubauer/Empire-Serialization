package com.troy.empireserialization;

public class EmpireSerializationSettings {
	public static final EmpireSerializationSettings defaultSettings = new EmpireSerializationSettings();
	
	static {
		defaultSettings.inferFinalClasses = true;
	}

	public boolean inferFinalClasses;

	public EmpireSerializationSettings() {

	}

	
	public static EmpireSerializationSettings getDefaultSettings() {
		return defaultSettings;
	}

}
