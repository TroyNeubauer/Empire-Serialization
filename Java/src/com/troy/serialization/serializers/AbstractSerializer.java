package com.troy.serialization.serializers;

public abstract class AbstractSerializer<T> implements Serializer<T> {
	protected Class<T> type;

	public AbstractSerializer(Class<T> type) {
		this.type = type;
	}

	@Override
	public Class<T> getType() {
		return type;
	}

}
