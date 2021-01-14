package net.steelphoenix.refraction.member;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A utility class for members.
 * All wrappers are cached.
 *
 * @author SteelPhoenix
 */
public class Members {

	private static final Map<Constructor<?>, IConstructor> CONSTRUCTOR_CACHE = new ConcurrentHashMap<>();
	private static final Map<Field, IField> FIELD_CACHE = new ConcurrentHashMap<>();
	private static final Map<Method, IMethod> METHOD_CACHE = new ConcurrentHashMap<>();
	private static final Map<Class<?>, IType> TYPE_CACHE = new ConcurrentHashMap<>();

	private Members() {
		// Nothing
	}

	/**
	 * Wrap a constructor.
	 *
	 * @param constructor Target constructor.
	 * @return the wrapped member.
	 */
	public static IConstructor wrap(Constructor<?> constructor) {
		// Preconditions
		if (constructor == null) {
			throw new NullPointerException("Constructor cannot be null");
		}

		return CONSTRUCTOR_CACHE.computeIfAbsent(constructor, member -> new SimpleConstructor(member));
	}

	/**
	 * Wrap a field.
	 *
	 * @param field Target field.
	 * @return the wrapped member.
	 */
	public static IField wrap(Field field) {
		// Preconditions
		if (field == null) {
			throw new NullPointerException("Field cannot be null");
		}

		return FIELD_CACHE.computeIfAbsent(field, member -> new SimpleField(member));
	}

	/**
	 * Wrap a method.
	 *
	 * @param method Target method.
	 * @return the wrapped member.
	 */
	public static IMethod wrap(Method method) {
		// Preconditions
		if (method == null) {
			throw new NullPointerException("Method cannot be null");
		}

		return METHOD_CACHE.computeIfAbsent(method, member -> new SimpleMethod(member));
	}

	/**
	 * Wrap a class.
	 *
	 * @param type Target class.
	 * @return the wrapped member.
	 */
	public static IType wrap(Class<?> type) {
		// Preconditions
		if (type == null) {
			throw new NullPointerException("Type cannot be null");
		}

		return TYPE_CACHE.computeIfAbsent(type, member -> new SimpleType(member));
	}
}
