package net.steelphoenix.refraction.wrapper;

import java.util.Objects;

/**
 * A base class for wrappers.
 *
 * @author SteelPhoenix
 */
public abstract class AbstractWrapper {

	private final Class<?> type;
	private Object handle;

	protected AbstractWrapper(Class<?> type) {
		if (type == null) {
			throw new NullPointerException("Type cannot be null");
		}

		this.type = type;
	}

	protected AbstractWrapper(Class<?> type, Object handle) {
		this(type);

		setHandle(handle);
	}

	/**
	 * Get the wrapped type.
	 *
	 * @return the type.
	 */
	public Class<?> getHandleType() {
		return type;
	}

	/**
	 * Get the wrapped instance.
	 *
	 * @return the handle.
	 */
	public Object getHandle() {
		// Preconditions
		if (handle == null) {
			throw new IllegalStateException("Handle is not set");
		}

		return handle;
	}

	/**
	 * Set the wrapped instance.
	 * Note that an instance can only be set once.
	 *
	 * @param handle New handle.
	 */
	protected void setHandle(Object handle) {
		// Preconditions
		if (this.handle != null) {
			throw new IllegalStateException("Handle is already set");
		}
		if (handle == null) {
			throw new NullPointerException("Handle cannot be null");
		}
		if (!type.isAssignableFrom(handle.getClass())) {
			throw new UnwrappableTypeException(handle + " is not of type " + type);
		}

		this.handle = handle;
	}

	@Override
	public int hashCode() {
		return handle == null ? 0 : handle.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof AbstractWrapper)) {
			return false;
		}

		return Objects.equals(handle, ((AbstractWrapper) object).handle);
	}

	@Override
	public String toString() {
		return getClass().getName() + "[handle=" + handle + "]";
	}
}
