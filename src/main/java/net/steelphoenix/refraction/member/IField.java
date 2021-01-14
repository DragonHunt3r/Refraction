package net.steelphoenix.refraction.member;

import java.lang.reflect.Field;

/**
 * A field wrapper.
 * We assume we can just get/set values without issues.
 *
 * @author SteelPhoenix
 */
public interface IField extends IMember {

	/**
	 * Get a field value.
	 * All reflective operation exceptions are wrapped in runtime exceptions.
	 *
	 * @param instance Instance to read from or null for static fields.
	 * @return the current value (may be null).
	 *
	 * @see #isStatic()
	 */
	public Object get(Object instance);

	/**
	 * Set a field value.
	 * All reflective operation exceptions are wrapped in runtime exceptions.
	 *
	 * @param instance Instance to write to or null for static fields.
	 * @param value New value.
	 *
	 * @see #isStatic()
	 */
	public void set(Object instance, Object value);

	/**
	 * Get the underlying field.
	 *
	 * @return the field.
	 */
	public Field getField();

	/**
	 * Get a read only version of this field.
	 * This instance throws an exception when calling {@link #set(Object, Object)}.
	 *
	 * @return a read-only instance.
	 */
	public IField asReadOnly();
}
