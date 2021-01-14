package net.steelphoenix.refraction.converter;

/**
 * An exception for when an object cannot be converted using a converter.
 *
 * @author SteelPhoenix
 */
public class InconvertibleTypeException extends IllegalArgumentException {

	private static final long serialVersionUID = -2476440132087835392L;

	public InconvertibleTypeException() {
		super();
	}

	public InconvertibleTypeException(String message) {
		super(message);
	}

	public InconvertibleTypeException(Throwable cause) {
		super(cause);
	}

	public InconvertibleTypeException(String message, Throwable cause) {
		super(message, cause);
	}
}
