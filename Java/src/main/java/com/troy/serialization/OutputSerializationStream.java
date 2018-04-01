package com.troy.serialization;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.troy.serialization.io.NativeFileOutput;
import com.troy.serialization.io.Output;
import com.troy.serialization.util.IntCache;

public class OutputSerializationStream implements ObjectOut {

	private Output out;
	private IntCache<String> stringCache;
	private IntCache<Class<?>> classCache;

	public OutputSerializationStream(Output out) {
		this.out = out;
	}

	public OutputSerializationStream(File file) {
		this.out = new NativeFileOutput(file);
	}

	public void writeObject(Object obj) {
		Class<?> clazz = obj.getClass();
		if(clazz == Byte.class) {
			System.out.println("Found!");
		} else if(clazz == Short.class) {
			
		} else if(clazz == Integer.class) {
			
		} else if(clazz == Long.class) {
			
		} else if(clazz == Float.class) {
			
		} else if(clazz == Double.class) {
			
		} else if(clazz == Character.class) {
			
		} else if(clazz == Boolean.class) {
			
		} else if(clazz == String.class) {
			
		} else if(clazz == List.class) {
			
		} else if(clazz == Set.class) {
			
		} else if(clazz == Map.class) {
			
		}
	}

	@Override
	public void writeByte(byte b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeShort(short s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeInt(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeLong(long l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeFloat(float f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeDouble(double d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeChar(char c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeBoolean(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeBigInteger(BigInteger integer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeBigDecimal(BigDecimal decimal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeArray(Object array) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeList(List<?> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeSet(Set<?> set) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeMap(Map<?, ?> set) {
		// TODO Auto-generated method stub
		
	}


}
