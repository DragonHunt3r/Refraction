package net.steelphoenix.refraction.member;

import java.lang.reflect.Method;

/**
 * A method wrapper.
 * We assume we can just invoke the method without issues.
 *
 * @author SteelPhoenix
 */
public interface IMethod extends IMember {

	/**
	 * Invoke the method.
	 * All reflective operation exceptions are wrapped in runtime exceptions.
	 *
	 * @param instance Instance to invoke the method on or null for static methods.
	 * @param args Method parameters.
	 * @return the returned value (may be null).
	 *
	 * @see #isStatic()
	 */
	public Object invoke(Object instance, Object... args);

	/**
	 * Get the underlying method.
	 *
	 * @return the method.
	 */
	public Method getMethod();
}
