package com.troy.serialization.serializers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import com.troy.serialization.io.Input;
import com.troy.serialization.io.Output;
import com.troy.serialization.util.MiscUtil;

import sun.misc.Unsafe;

public class FieldSerializer<T> extends AbstractSerializer<T> {
	
	public FieldSerializer(Class<T> type) {
		super(type);
	}

	private static final Unsafe unsafe = MiscUtil.getUnsafe();

	public T newInstance() {
		return null;
	}

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
