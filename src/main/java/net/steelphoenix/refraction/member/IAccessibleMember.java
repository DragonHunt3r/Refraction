package net.steelphoenix.refraction.member;

import java.lang.reflect.Modifier;

/**
 * Represents an accessible member.
 * Note that some methods may not be relevant for certain implementing classes.
 *
 * @author SteelPhoenix
 */
// TODO: Setters?
@FunctionalInterface
public interface IAccessibleMember {

	/**
	 * Get the Java language modifiers for this member.
	 *
	 * @return the modifiers.
	 */
	public int getModifiers();

	/**
	 * Get if this member is abstract.
	 *
	 * @return if this member is abstract.
	 */
	public default boolean isAbstract() {
		return Modifier.isAbstract(getModifiers());
	}

	/**
	 * Get if this member is final.
	 *
	 * @return if this member is final.
	 */
	public default boolean isFinal() {
		return Modifier.isFinal(getModifiers());
	}

	/**
	 * Get if this member is an interface.
	 *
	 * @return if this member is an interface.
	 */
	public default boolean isInterface() {
		return Modifier.isInterface(getModifiers());
	}

	/**
	 * Get if this member is native.
	 *
	 * @return if this member is native.
	 */
	public default boolean isNative() {
		return Modifier.isNative(getModifiers());
	}

	/**
	 * Get if this member is package private.
	 * This is technically not a modifier, but rather the absence of any visibility modifiers.
	 *
	 * @return if this member is package private.
	 */
	public default boolean isPackagePrivate() {
		return (getModifiers() & (Modifier.PRIVATE | Modifier.PROTECTED | Modifier.PUBLIC)) == 0;
	}

	/**
	 * Get if this member is private.
	 *
	 * @return if this member is private.
	 */
	public default boolean isPrivate() {
		return Modifier.isPrivate(getModifiers());
	}

	/**
	 * Get if this member is protected.
	 *
	 * @return if this member is protected.
	 */
	public default boolean isProtected() {
		return Modifier.isProtected(getModifiers());
	}

	/**
	 * Get if this member is public.
	 *
	 * @return if this member is public.
	 */
	public default boolean isPublic() {
		return Modifier.isPublic(getModifiers());
	}

	/**
	 * Get if this member is static.
	 *
	 * @return if this member is static.
	 */
	public default boolean isStatic() {
		return Modifier.isStatic(getModifiers());
	}

	/**
	 * Get if this member is strict.
	 *
	 * @return if this member is strict.
	 */
	public default boolean isStrict() {
		return Modifier.isStrict(getModifiers());
	}

	/**
	 * Get if this member is synchronized.
	 *
	 * @return if this member is synchronized.
	 */
	public default boolean isSynchronized() {
		return Modifier.isSynchronized(getModifiers());
	}

	/**
	 * Get if this member is transient.
	 *
	 * @return if this member is transient.
	 */
	public default boolean isTransient() {
		return Modifier.isTransient(getModifiers());
	}

	/**
	 * Get if this member is volatile.
	 *
	 * @return if this member is volatile.
	 */
	public default boolean isVolatile() {
		return Modifier.isVolatile(getModifiers());
	}
}