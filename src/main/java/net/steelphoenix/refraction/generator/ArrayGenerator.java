package net.steelphoenix.refraction.generator;

import java.lang.reflect.Array;

/**
 * A generator generating arrays.
 * All arrays will have size 0.
 * Note that this is a singleton.
 *
 * @author SteelPhoenix
 */
public class ArrayGenerator implements IValueGenerator {

	private static final IValueGenerator INSTANCE = new ArrayGenerator();

	private ArrayGenerator() {
		// Nothing
	}

	@Override
	public Object generate(Class<?> type) {
		// Preconditions
		if (type == null) {
			throw new NullPointerException("Type cannot be null");
		}

		// Incorrect type
		if (!type.isArray()) {
			throw new UngeneratableTypeException("Type is not an array");
		}

		return Array.newInstance(type.getComponentType(), 0);
	}

	/**
	 * Get the instance.
	 *
	 * @return the instance.
	 */
	public static IValueGenerator getInstance() {
		return INSTANCE;
	}
}
