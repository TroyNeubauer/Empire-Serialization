package com.troy.empireserialization;

public class SerializationSettings {
	public static final SerializationSettings defaultSettings = new SerializationSettings();


	public boolean inferFinalClasses;
	public boolean useObjectCache, useStringCache;

	public boolean useVLE;
	public boolean useLittleEndian;
	public boolean pluginsPresent;
	public int version = Version.VERSION_1;

	public SerializationSettings() {
		inferFinalClasses = true;
		useVLE = true;
		useObjectCache = true;
		useStringCache = true;
	}

	public SerializationSettings inferFinalClasses(boolean inferFinalClasses) {
		this.inferFinalClasses = inferFinalClasses;
		return this;
	}

	public SerializationSettings useObjectCache(boolean useObjectCache) {
		this.useObjectCache = useObjectCache;
		return this;
	}

	public SerializationSettings useStringCache(boolean useStringCache) {
		this.useStringCache = useStringCache;
		return this;
	}

	public SerializationSettings useVLE(boolean useVLE) {
		this.useVLE = useVLE;
		return this;
	}

	public SerializationSettings useLittleEndian(boolean useLittleEndian) {
		this.useLittleEndian = useLittleEndian;
		return this;
	}

	public static SerializationSettings getDefaultSettings() {
		return defaultSettings;
	}

	public int getFlagsData() {

		return 0;
	}

}
