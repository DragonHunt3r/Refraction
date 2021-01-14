package net.steelphoenix.refraction.generator;

/**
 * A generator generating null values for everything.
 * Note that some types (like primitives) do not support null.
 * Note that this is a singleton.
 *
 * @author SteelPhoenix
 */
public class NullGenerator implements IValueGenerator {

	private static final IValueGenerator INSTANCE = new NullGenerator();

	private NullGenerator() {
		// Nothing
	}

	@Override
	public Object generate(Class<?> type) {
		// Preconditions
		if (type == null) {
			throw new NullPointerException("Type cannot be null");
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
