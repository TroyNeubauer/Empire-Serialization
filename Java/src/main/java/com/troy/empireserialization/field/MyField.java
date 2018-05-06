package com.troy.empireserialization.field;

import java.lang.reflect.Field;

import com.troy.empireserialization.EmpireInput;
import com.troy.empireserialization.EmpireOutput;
import com.troy.empireserialization.io.in.Input;
import com.troy.empireserialization.io.out.Output;
import com.troy.empireserialization.util.MiscUtil;

import sun.misc.Unsafe;

public abstract class MyField {
	protected static final Unsafe unsafe = MiscUtil.getUnsafe();
	
	protected Class<?> type;
	protected String name;
	protected long offset;
	protected Field rawField;

	public abstract void writeField(Object parent, Output out, EmpireOutput empOut);

	public abstract void readField(Object parent, Input in, EmpireInput empIn);
}
