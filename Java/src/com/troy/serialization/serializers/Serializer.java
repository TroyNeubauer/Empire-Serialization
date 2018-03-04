package com.troy.serialization.serializers;

import com.troy.serialization.io.*;

public interface Serializer<T> {
	
	public Class<T> getType();
	
	public void writeFields(Output out);
	
	public void readFields(Input in);
}
