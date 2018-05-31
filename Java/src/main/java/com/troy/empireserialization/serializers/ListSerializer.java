package com.troy.empireserialization.serializers;

import java.util.List;

import com.troy.empireserialization.ObjectIn;
import com.troy.empireserialization.ObjectOut;
import com.troy.empireserialization.SerializationSettings;
import com.troy.empireserialization.io.in.Input;
import com.troy.empireserialization.io.out.Output;
import com.troy.empireserialization.util.ReflectionUtils;

public class ListSerializer extends AbstractObjectSerializer<List> {

	public static final ListSerializer SERIALIZER = new ListSerializer();
	
	public ListSerializer() {
		super(List.class);
	}

	@Override
	public void write(ObjectOut objectOut, List obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
		Object[] data = ReflectionUtils.getListData(obj);
		if(extra == -1) {//Keep the offset at 0 but put in the length
			extra = obj.size();
		}
		objectOut.writeObjectRecursive(data, extra);
	}

	@Override
	public void read(ObjectIn objIn, List obj, Input in, Class<List> type, long extra) {
		Object[] listData = ReflectionUtils.getListData(obj);
		ArraySerializer.SERIALIZER.read(objIn, listData, in, (Class<Object[]>) listData.getClass(), extra);
	}
}
