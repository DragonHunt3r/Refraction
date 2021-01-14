package net.steelphoenix.refraction.primitives;

/**
 * An enum for Java primitives.
 * Note that this does not include String.
 *
 * @author SteelPhoenix
 */
public enum Primitives implements IPrimitive {

	/**
	 * A Java boolean.
	 */
	BOOLEAN (Boolean.FALSE, Boolean.class, boolean.class),

	/**
	 * A Java byte.
	 */
	BYTE (Byte.valueOf((byte) 0), Byte.class, byte.class),

	/**
	 * A Java char.
	 */
	CHARACTER (Character.valueOf('\0'), Character.class, char.class),

	/**
	 * A Java double.
	 */
	DOUBLE (Double.valueOf(0D), Double.class, double.class),

	/**
	 * A Java float.
	 */
	FLOAT (Float.valueOf(0F), Float.class, float.class),

	/**
	 * A Java int.
	 */
	INTEGER (Integer.valueOf(0), Integer.class, int.class),

	/**
	 * A Java long.
	 */
	LONG (Long.valueOf(0L), Long.class, long.class),

	/**
	 * A Java short.
	 */
	SHORT (Short.valueOf((short) 0), Short.class, short.class);

	private final Object def;
	private final Class<?> wrapped;
	private final Class<?> primitive;
	private Primitives(Object def, Class<?> wrapped, Class<?> primitive) {
		this.def = def;
		this.wrapped = wrapped;
		this.primitive = primitive;
	}

	@Override
	public Object getDefaultValue() {
		return def;
	}

	@Override
	public Class<?> getBoxedType() {
		return wrapped;
	}

	@Override
	public Class<?> getPrimitiveType() {
		return primitive;
	}

	@Override
	public boolean isAssignable(Class<?> type) {
		return type == getBoxedType() || type == getPrimitiveType();
	}

	/**
	 * Get the primitive type for a given type.
	 *
	 * @param type Target type.
	 * @return the primitive type associated with this type or null if no matching primitive is found.
	 */
	public static IPrimitive getByType(Class<?> type) {
		// Preconditions
		if (type == null) {
			throw new NullPointerException("Type cannot be null");
		}

		for (IPrimitive primitive : values()) {
			if (primitive.isAssignable(type)) {
				return primitive;
			}
		}

		// Not a primitive
		return null;
	}
}