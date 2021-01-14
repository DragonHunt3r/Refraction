package net.steelphoenix.refraction.generator;

/**
 * An exception for when no instance can be generated for a given type.
 *
 * @author SteelPhoenix
 */
public class UngeneratableTypeException extends IllegalArgumentException {

	private static final long serialVersionUID = -8796919856635380660L;

	public UngeneratableTypeException() {
		super();
	}
	public UngeneratableTypeException(String message) {
		super(message);
	}
	public UngeneratableTypeException(Throwable cause) {
		super(cause);
	}
	public UngeneratableTypeException(String message, Throwable cause) {
		super(message, cause);
	}
}
