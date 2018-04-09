package com.troy.empireserialization.serializers;

import com.troy.empireserialization.io.in.*;
import com.troy.empireserialization.io.out.*;
import com.troy.empireserialization.util.*;

import sun.misc.*;

public class FieldSerializer<T> extends AbstractSerializer<T> {
	
	public FieldSerializer(Class<T> type) {
		super(type);
	}

	private static final Unsafe unsafe = MiscUtil.getUnsafe();

	@Override
	public void writeFields(Object obj, Output out) {
		if(unsafe != null) {
			
		} else {
			
		}
	}

	@Override
	public T readFields(Object obj, Input in) {
		return null;
	}
}
