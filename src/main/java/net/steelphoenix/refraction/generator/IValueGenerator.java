package net.steelphoenix.refraction.generator;

/**
 * An instance provider.
 *
 * @author SteelPhoenix
 */
@FunctionalInterface
public interface IValueGenerator {

	/**
	 * Attempt to generate a value for the given type.
	 * If it cannot generate a value an {@link UngeneratableTypeException} should be thrown.
	 *
	 * @param type Target type.
	 * @return the generated value.
	 */
	public Object generate(Class<?> type);
}