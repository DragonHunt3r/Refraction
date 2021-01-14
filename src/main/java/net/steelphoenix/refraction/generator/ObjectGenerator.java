package net.steelphoenix.refraction.generator;

/**
 * A generator generating null values for all objects.
 * Note that this is a singleton.
 *
 * @author SteelPhoenix
 */
public class ObjectGenerator implements IValueGenerator {

	private static final IValueGenerator INSTANCE = new ObjectGenerator();

	private ObjectGenerator() {
		// Nothing
	}

	@Override
	public Object generate(Class<?> type) {
		// Preconditions
		if (type == null) {
			throw new NullPointerException("Type cannot be null");
		}

		if (!Object.class.isAssignableFrom(type)) {
			throw new UngeneratableTypeException("Type is not an object");
		}

		return null;
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
