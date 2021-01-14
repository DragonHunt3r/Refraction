package net.steelphoenix.refraction.member;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import net.steelphoenix.refraction.Util;

/**
 * A default constructor wrapper implementation.
 *
 * @author SteelPhoenix
 */
public class SimpleConstructor implements IConstructor {

	private final Constructor<?> constructor;

	public SimpleConstructor(Constructor<?> constructor) {
		if (constructor == null) {
			throw new NullPointerException("Constructor cannot be null");
		}

		this.constructor = constructor;
	}

	@Override
	public Object invoke(Object... args) {
		try {
			return constructor.newInstance(args);
		} catch (IllegalAccessException | InstantiationException exception) {
			// Not invokable
			throw new IllegalStateException("Cannot invoke constructor", exception);
		} catch (InvocationTargetException exception) {
			// Constructor itself threw an exception
			throw new RuntimeException("An internal error occured", exception.getCause());
		}
	}

	@Override
	public Constructor<?> getConstructor() {
		return constructor;
	}

	@Override
	public int getModifiers() {
		return constructor.getModifiers();
	}

	@Override
	public Collection<Annotation> getAnnotations() {
		return Arrays.stream(constructor.getDeclaredAnnotations()).collect(Collectors.toSet());
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> clazz) {
		// Preconditions
		if (clazz == null) {
			throw new NullPointerException("Type cannot be null");
		}

		return constructor.getDeclaredAnnotation(clazz);
	}

	@Override
	public boolean isSynthetic() {
		return constructor.isSynthetic();
	}

	@Override
	public Class<?> getDeclarer() {
		return constructor.getDeclaringClass();
	}

	@Override
	public String getName() {
		return constructor.getName();
	}

	@Override
	public int hashCode() {
		return constructor.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof IConstructor)) {
			return false;
		}
		return constructor.equals(((IConstructor) object).getConstructor());
	}

	@Override
	public String toString() {
		return "Constructor[constructor=" + Util.constructorToString(constructor) + "]";
	}
}
