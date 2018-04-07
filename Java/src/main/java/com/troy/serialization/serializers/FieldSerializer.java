package com.troy.serialization.serializers;

import com.troy.serialization.io.in.*;
import com.troy.serialization.io.out.*;
import com.troy.serialization.util.*;

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
