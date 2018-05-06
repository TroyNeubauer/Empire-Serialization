package com.troy.empireserialization.serializers;

import com.troy.empireserialization.ClassIDProvider;
import com.troy.empireserialization.clazz.ClassData;
import com.troy.empireserialization.io.out.*;

public abstract class AbstractSerializer<T> implements Serializer<T> {
	protected Class<T> type;
	protected ClassData<T> data;

	public AbstractSerializer(Class<T> type, ClassIDProvider provider) {
		this.type = type;
		this.data = new ClassData<T>(type, provider);
	}

	@Override
	public Class<T> getType() {
		return type;
	}
	
	public T newInstance() {
		return null;//Delegate. Sub classes can override this if necessary
	}
	
	@Override
	public void writeTypeDefinition(Output out) {
		out.writeBytes(data.getTypeDefinition());
	}
	
	


}
