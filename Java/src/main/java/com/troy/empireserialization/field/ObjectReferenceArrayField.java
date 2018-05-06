package com.troy.empireserialization.field;

import com.troy.empireserialization.EmpireInput;
import com.troy.empireserialization.EmpireOutput;
import com.troy.empireserialization.io.in.Input;
import com.troy.empireserialization.io.out.Output;

public class ObjectReferenceArrayField extends MyField {

	@Override
	public void writeField(Object parent, Output out, EmpireOutput empOut) {
		Object field;
		if (unsafe != null) {
			field = unsafe.getObject(parent, offset);
		} else {
			try {
				field = rawField.get(parent);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		empOut.writeArray((Object[]) field);
	}

	@Override
	public void readField(Object parent, Input in, EmpireInput empIn) {
		Object field = empIn.readArray();
		if (unsafe != null) {
			unsafe.getAndSetObject(parent, offset, field);
		} else {
			try {
				rawField.set(parent, field);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
