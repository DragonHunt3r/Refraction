package net.steelphoenix.refraction.reflection;

/**
 * An exception for when a requested element cannot be found.
 *
 * @author SteelPhoenix
 */
public class UnknownElementException extends RuntimeException {
	private static final long serialVersionUID = -5961699835182725045L;

	public UnknownElementException() {
		super();
	}

	public UnknownElementException(String message) {
		super(message);
	}

	public UnknownElementException(Throwable cause) {
		super(cause);
	}

	public UnknownElementException(String message, Throwable cause) {
		super(message, cause);
	}
}
