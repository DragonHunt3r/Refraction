package net.steelphoenix.refraction.generator;

import net.steelphoenix.refraction.primitives.IPrimitive;
import net.steelphoenix.refraction.primitives.Primitives;

/**
 * A generator generating default values for primitives.
 * Note that this does not generate values for Strings.
 * Note that this is a singleton.
 *
 * @author SteelPhoenix
 */
public class PrimitiveGenerator implements IValueGenerator {

	private static final IValueGenerator INSTANCE = new PrimitiveGenerator();

	private PrimitiveGenerator() {
		// Nothing
	}

	@Override
	public Object generate(Class<?> type) {
		// Preconditions
		if (type == null) {
			throw new NullPointerException("Type cannot be null");
		}

		// Primitives
		IPrimitive primitive = Primitives.getByType(type);

		if (primitive == null) {
			throw new UngeneratableTypeException("Type is not a primitive");
		}

		return primitive.getDefaultValue();
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