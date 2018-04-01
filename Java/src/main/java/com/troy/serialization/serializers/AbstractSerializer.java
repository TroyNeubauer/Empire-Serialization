package com.troy.serialization.serializers;

import com.troy.serialization.io.*;

public abstract class AbstractSerializer<T> implements Serializer<T> {
	protected Class<T> type;

	public AbstractSerializer(Class<T> type) {
		this.type = type;
	}

	@Override
	public Class<T> getType() {
		return type;
	}
	
	@Override
	public void writeTypeDefinition(Output out) {
		// TODO Auto-generated method stub
		
	}

}
