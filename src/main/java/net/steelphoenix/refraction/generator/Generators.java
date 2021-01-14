package net.steelphoenix.refraction.generator;

import java.util.Arrays;

/**
 * A utility class for value generators.
 *
 * @author SteelPhoenix
 */
public class Generators {

	private static final IValueGenerator DEFAULT = of(getPrimitiveGenerator(), getStringGenerator(), getArrayGenerator(), getCollectionGenerator(), getObjectGenerator(), getNullGenerator());
	private static IValueGenerator def = DEFAULT;

	private Generators() {
		// Nothing
	}

	/**
	 * Get the default value generator.
	 * By default this is a joint generator for most fields (arrays, primitives, collections, objects, strings etc).
	 *
	 * @return the value generator.
	 */
	public static IValueGenerator getDefaultGenerator() {
		return def;
	}

	/**
	 * Set the default value generator.
	 *
	 * @param generator Generator to use or null to reset to the original value generator.
	 */
	public static void setDefaultGenerator(IValueGenerator generator) {
		// Reset
		if (generator == null) {
			def = DEFAULT;
		}

		def = generator;
	}

	/**
	 * Get the value generator for arrays.
	 *
	 * @return the value generator.
	 */
	public static IValueGenerator getArrayGenerator() {
		return ArrayGenerator.getInstance();
	}

	/**
	 * Get the value generator for collections and maps.
	 *
	 * @return the value generator.
	 */
	public static IValueGenerator getCollectionGenerator() {
		return CollectionGenerator.getInstance();
	}

	/**
	 * Get a value generator for anything, returning null.
	 *
	 * @return the value generator.
	 */
	public static IValueGenerator getNullGenerator() {
		return NullGenerator.getInstance();
	}

	/**
	 * Get the value generator for objects.
	 *
	 * @return the value generator.
	 */
	public static IValueGenerator getObjectGenerator() {
		return ObjectGenerator.getInstance();
	}

	/**
	 * Get the value generator for primitives.
	 *
	 * @return the value generator.
	 */
	public static IValueGenerator getPrimitiveGenerator() {
		return PrimitiveGenerator.getInstance();
	}

	/**
	 * Get the value generator for strings.
	 *
	 * @return the value generator.
	 */
	public static IValueGenerator getStringGenerator() {
		return StringGenerator.getInstance();
	}

	/**
	 * Get a generator using multiple generators in the array's order.
	 * Note that we assume the developer is smart enough to not create circular structures.
	 *
	 * @param generators Generators to fall back on.
	 * @return the joined generator.
	 */
	public static IValueGenerator of(IValueGenerator... generators) {
		// Preconditions
		if (generators == null) {
			throw new NullPointerException("Generators cannot be null");
		}

		// We clone it so it cannot be modified
		return new ValueGenerator(generators.clone());
	}

	/**
	 * A generator using multiple underlying generators.
	 * This generator will throw an exception if no value can be generated.
	 *
	 * @author SteelPhoenix
	 */
	private static class ValueGenerator implements IValueGenerator {

		private final IValueGenerator[] generators;

		private ValueGenerator(IValueGenerator[] generators) {
			if (generators == null) {
				throw new NullPointerException("Generators cannot be null");
			}

			this.generators = generators;
		}

		@Override
		public Object generate(Class<?> type) {
			// Preconditions
			if (type == null) {
				throw new NullPointerException("Type cannot be null");
			}

			boolean generated = false;
			Object value = null;
			for (IValueGenerator generator : generators) {

				// Too lazy to check for null values beforehand so we just ignore them
				if (generator == null) {
					continue;
				}

				// Try to generate
				try {
					value = generator.generate(type);
					generated = true;
				} catch (UngeneratableTypeException exception) {
					// Nothing
				}

				if (generated) {
					break;
				}
			}

			// Not generated
			if (!generated) {
				throw new UngeneratableTypeException("Cannot generate value for " + type + " in joined generator " + this);
			}

			return value;
		}

		@Override
		public int hashCode() {
			return Arrays.hashCode(generators);
		}

		@Override
		public boolean equals(Object object) {
			if (!(object instanceof ValueGenerator)) {
				return false;
			}

			return Arrays.equals(generators, ((ValueGenerator) object).generators);
		}

		@Override
		public String toString() {
			return "ValueGenerator[generators=" + Arrays.toString(generators) + "]";
		}
	}
}
