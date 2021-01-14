package net.steelphoenix.refraction.member;

import java.lang.reflect.Constructor;

/**
 * A constructor wrapper.
 * We assume we can just construct an instance without issues.
 *
 * @author SteelPhoenix
 */
public interface IConstructor extends IMember {

	/**
	 * Invoke the constructor.
	 * All reflective operation exceptions are wrapped in runtime exceptions.
	 *
	 * @param args Constructor parameters.
	 * @return the constructed instance.
	 */
	public Object invoke(Object... args);

	/**
	 * Get the underlying constructor.
	 *
	 * @return the constructor.
	 */
	public Constructor<?> getConstructor();
}
