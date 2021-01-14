package net.steelphoenix.refraction.generator;

/**
 * A generator generating default values for strings.
 * Note that this is a singleton.
 *
 * @author SteelPhoenix
 */
public class StringGenerator implements IValueGenerator {

	private static final IValueGenerator INSTANCE = new StringGenerator();

	private StringGenerator() {
		// Nothing
	}

	@Override
	public Object generate(Class<?> type) {
		// Preconditions
		if (type == null) {
			throw new NullPointerException("Type cannot be null");
		}

		if (type != String.class) {
			throw new UngeneratableTypeException("Type is not a string");
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
