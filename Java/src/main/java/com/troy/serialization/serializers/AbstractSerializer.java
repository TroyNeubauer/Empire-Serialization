package com.troy.serialization.serializers;

import com.troy.serialization.io.*;

public abstract class AbstractSerializer<T> implements Serializer<T> {
	protected Class<T> type;
	protected ClassData<T> data;

	public AbstractSerializer(Class<T> type) {
		this.type = type;
		this.data = new ClassData<T>(type);
	}

	@Override
	public Class<T> getType() {
		return type;
	}
	
	@Override
	public void writeTypeDefinition(Output out) {
		out.writeBytes(data.getTypeDefinition());
	}


}
