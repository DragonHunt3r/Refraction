package net.steelphoenix.refraction.primitives;

/**
 * A primitive.
 *
 * @author SteelPhoenix
 */
public interface IPrimitive {

	/**
	 * Get the default value.
	 *
	 * @return the default value.
	 */
	public Object getDefaultValue();

	/**
	 * Get the wrapper class of this primitive.
	 *
	 * @return the box class.
	 */
	public Class<?> getBoxedType();

	/**
	 * Get the primitive class of this primitive.
	 *
	 * @return the primitive class.
	 */
	public Class<?> getPrimitiveType();

	/**
	 * Check if a class is assignable to this primitive.
	 *
	 * @param type Target type.
	 * @return if the type is assignable to this primitive type.
	 */
	public boolean isAssignable(Class<?> type);
}
