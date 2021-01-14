package net.steelphoenix.refraction.converter;

/**
 * A converter.
 * Note that the generic type is not specified, as it may not be known.
 *
 * @param <T> Type to convert to.
 *
 * @author SteelPhoenix
 */
public interface IConverter<T> {

	/**
	 * Convert to the specific type.
	 * This may throw an {@link InconvertibleTypeException} if the generic type is of the incorrect type.
	 *
	 * @param generic Generic instance to convert.
	 * @return the specific instance.
	 */
	public T getSpecific(Object generic);

	/**
	 * Convert to the generic type.
	 * This should never throw an {@link InconvertibleTypeException} as the specific type is set.
	 *
	 * @param specific Specific instance to convert.
	 * @return the generic instance.
	 */
	public Object getGeneric(T specific);

	/**
	 * Get the specific type.
	 *
	 * @return the specific type.
	 */
	public Class<T> getSpecificType();
}
