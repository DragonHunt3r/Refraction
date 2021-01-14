package net.steelphoenix.refraction.structure;

/**
 * An exception for when a field cannot be accessed.
 *
 * @author SteelPhoenix
 */
public class FieldAccessException extends RuntimeException {
	private static final long serialVersionUID = -5559127970534232038L;

	public FieldAccessException() {
		super();
	}

	public FieldAccessException(String message) {
		super(message);
	}

	public FieldAccessException(Throwable cause) {
		super(cause);
	}

	public FieldAccessException(String message, Throwable cause) {
		super(message, cause);
	}
}
