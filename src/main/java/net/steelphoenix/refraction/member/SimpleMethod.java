package net.steelphoenix.refraction.member;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import net.steelphoenix.refraction.Util;

/**
 * A default method wrapper implementation.
 *
 * @author SteelPhoenix
 */
public class SimpleMethod implements IMethod {

	private final Method method;

	public SimpleMethod(Method method) {
		if (method == null) {
			throw new NullPointerException("Method cannot be null");
		}

		this.method = method;
	}

	@Override
	public Object invoke(Object instance, Object... args) {
		// Preconditions
		if (!isStatic() && instance == null) {
			// No NPE because technically null instances are supported if the method is static
			throw new IllegalArgumentException("Instance cannot be null for non-static methods");
		}

		try {
			return method.invoke(instance, args);
		} catch (IllegalAccessException exception) {
			// Not invokable
			throw new IllegalStateException("Cannot invoke constructor", exception);
		} catch (InvocationTargetException exception) {
			// Method itself threw an exception
			throw new RuntimeException("An internal error occured", exception.getCause());
		}
	}

	@Override
	public Method getMethod() {
		return method;
	}

	@Override
	public int getModifiers() {
		return method.getModifiers();
	}

	@Override
	public Collection<Annotation> getAnnotations() {
		return Arrays.stream(method.getDeclaredAnnotations()).collect(Collectors.toSet());
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> clazz) {
		// Preconditions
		if (clazz == null) {
			throw new NullPointerException("Type cannot be null");
		}

		return method.getDeclaredAnnotation(clazz);
	}

	@Override
	public boolean isSynthetic() {
		return method.isSynthetic();
	}

	@Override
	public Class<?> getDeclarer() {
		return method.getDeclaringClass();
	}

	@Override
	public String getName() {
		return method.getName();
	}

	@Override
	public int hashCode() {
		return method.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof IMethod)) {
			return false;
		}

		return method.equals(((IMethod) object).getMethod());
	}

	@Override
	public String toString() {
		return "Method[method=" + Util.methodToString(method) + "]";
	}
}
