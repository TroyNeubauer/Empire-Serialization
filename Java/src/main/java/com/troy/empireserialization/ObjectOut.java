package com.troy.empireserialization;

import java.io.Flushable;

import com.troy.empireserialization.cache.IntValue;
import com.troy.empireserialization.cache.IntValueCache;

public interface ObjectOut extends Flushable, AutoCloseable {

	public <T> void writeObject(T obj);

	public void flush();

	public void close();

	/**
	 * Called recursively to write an instance variable of an object
	 * 
	 * @param object
	 *            The object to write
	 */
	<T> void writeObjectRecursive(T object, long extra);
	
	public void writeString(String str);
	
	void writeTypeReference(IntValue<Class<?>> entry);

	void writeTypeDefinition(Class<?> type);
	
	public void writeTypeComplete(Class<?> type);

	public IntValueCache<Object> getObjectCache();

	public IntValueCache<Class<?>> getClassCache();

	public IntValueCache<String> getStringCache();

}
