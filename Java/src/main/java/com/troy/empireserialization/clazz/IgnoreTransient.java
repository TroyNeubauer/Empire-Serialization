package com.troy.empireserialization.clazz;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Informs the serializer that it should ignore the {@code transient} modifier on a class of field. 
 * If used on a class, the serializer will treat all fields as if they do not have the transient modifier.
 * If used on a field, the serializer will treat only that field as if it is missing the transient modifier. 
 * @author Troy Neubauer
 *
 */

@Retention(RUNTIME)
@Target({ TYPE, FIELD })
public @interface IgnoreTransient {

}
