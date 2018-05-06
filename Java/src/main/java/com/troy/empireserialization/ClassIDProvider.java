package com.troy.empireserialization;

public interface ClassIDProvider {
	
	/**
	 * Returns the integer identifier bound to the specified parameter or -1 if none exists.
	 * 
	 * @param type
	 *            The class to use in the ID lookup
	 * @return The ID associated with {@code type} or -1 is there is none exists
	 */
	public int getTypeID(Class<?> type);
}
