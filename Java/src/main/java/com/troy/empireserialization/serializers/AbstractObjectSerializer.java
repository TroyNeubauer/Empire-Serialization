package com.troy.empireserialization.serializers;

import com.troy.empireserialization.EmpireOutput;
import com.troy.empireserialization.util.ClassHelper;

public abstract class AbstractObjectSerializer<T> implements ObjectSerializer<T> {
	protected Class<T> type;

	public AbstractObjectSerializer(Class<T> type) {
		this.type = type;
	}

	@Override
	public Class<T> getType() {
		return type;
	}

	public T newInstance() {
		return null;// Delegate. Sub classes can override this if necessary
	}

	@Override
	public void writeTypeDefinition(EmpireOutput out) {
		ClassHelper.getClassData(type).writeTypeDefinition(out);
	}

}
