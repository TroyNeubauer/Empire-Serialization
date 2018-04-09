package com.troy.empireserialization.serializers;

import com.troy.empireserialization.io.in.*;
import com.troy.empireserialization.io.out.*;

public interface Serializer<T> {

	public Class<T> getType();

	/**
	 * Writes an object's fields to the output stream so that It can be de-serialized later using
	 * {@link #readFields(Object, Input)}
	 * 
	 * @param obj
	 *            The object to write
	 * @param out
	 *            The output to write to
	 */
	public void writeFields(T obj, Output out);

	/**
	 * 
	 * @param Reads
	 *            from the input provided to assign the fields of the object passed in.
	 * @param obj
	 *            The object whose fields should be assigned
	 * @param in
	 *            The input to read from
	 */
	public T readFields(Object obj, Input in);

	/**
	 * Returns a new instance of the type represented by this serializer. If an instance cannot be created by this
	 * serializer for any reason, it should return null to delegate instance creation to the current instantiation strategy.
	 * 
	 * @return A new instance of the type represented by this serializer
	 */
	public T newInstance();

	/**
	 * Writes a TS Standard compliant type definition for the type this serializer represents
	 * 
	 * @param out
	 *            The output to write the definition to
	 */
	public void writeTypeDefinition(Output out);
}
