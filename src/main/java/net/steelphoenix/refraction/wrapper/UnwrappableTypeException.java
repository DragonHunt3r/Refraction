package net.steelphoenix.refraction.wrapper;

/**
 * An exception for when an object cannot be wrapped.
 *
 * @author SteelPhoenix
 */
public class UnwrappableTypeException extends IllegalArgumentException {

	private static final long serialVersionUID = -6160308073913084824L;

	public UnwrappableTypeException() {
		super();
	}

	public UnwrappableTypeException(String message) {
		super(message);
	}

	public UnwrappableTypeException(Throwable cause) {
		super(cause);
	}

	public UnwrappableTypeException(String message, Throwable cause) {
		super(message, cause);
	}
}
