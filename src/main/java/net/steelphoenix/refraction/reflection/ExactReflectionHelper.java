package net.steelphoenix.refraction.reflection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import net.steelphoenix.refraction.Util;
import net.steelphoenix.refraction.member.IConstructor;
import net.steelphoenix.refraction.member.IField;
import net.steelphoenix.refraction.member.IMethod;
import net.steelphoenix.refraction.reflection.predicate.ConstructorQueryMatcher;
import net.steelphoenix.refraction.reflection.predicate.FieldQueryMatcher;
import net.steelphoenix.refraction.reflection.predicate.Matchers;
import net.steelphoenix.refraction.reflection.predicate.MethodQueryMatcher;

/**
 * A reflection helper for exact member matching.
 * Note that query results are cached if successful.
 *
 * @author SteelPhoenix
 */
public class ExactReflectionHelper extends ReflectionHelper {

	private final Map<Class<?>[], IConstructor> constructorCache = new ConcurrentHashMap<>();
	private final Map<String, IField> fieldCache = new ConcurrentHashMap<>();
	private final Map<Entry<String, Class<?>[]>, IMethod> methodCache = new ConcurrentHashMap<>();

	public ExactReflectionHelper(Class<?> clazz) {
		super(clazz);
	}

	public ExactReflectionHelper(Class<?> clazz, boolean force) {
		super(clazz, force);
	}

	/**
	 * Get a constructor by parameter types.
	 *
	 * @param params Constructor parameter types.
	 * @return the constructor.
	 */
	public IConstructor getConstructor(Class<?>... params) {
		// Preconditions
		if (params == null) {
			throw new NullPointerException("Parameters cannot be null");
		}
		for (int i = 0; i < params.length; i++) {
			if (params[i] == null) {
				throw new NullPointerException("Parameter cannot be null");
			}
		}

		// Cache
		if (constructorCache.containsKey(params)) {
			return constructorCache.get(params);
		}

		// Query
		ConstructorQueryMatcher.Builder builder = Matchers.ConstructorMatcher.newBuilder().withParameters(params);
		if (!isForceAccess()) {
			builder.withModifierPublic();
		}

		// It would not make sense to provide an instance or to use recursion here
		List<IConstructor> result = getConstructors(getSource(), null, builder.build(), false);


		// No result
		if (result.isEmpty()) {
			throw new UnknownElementException("No such constructor in " + getSource().getName() + ": " + Util.constructorToString(getSource(), params));
		}

		// There should not be any other elements
		IConstructor constructor = result.get(0);

		constructorCache.put(params, constructor);

		return constructor;
	}

	/**
	 * Get a field by name.
	 *
	 * @param name Field name.
	 * @return the field.
	 */
	public IField getField(String name) {
		return getField(name, null);
	}

	/**
	 * Get a field by name.
	 *
	 * @param name Field name.
	 * @param instance Instance.
	 * @return the field.
	 */
	public IField getField(String name, Object instance) {
		// Preconditions
		if (name == null) {
			throw new NullPointerException("Name cannot be null");
		}

		// Cache
		if (instance == null && fieldCache.containsKey(name)) {
			return fieldCache.get(name);
		}

		// Query
		FieldQueryMatcher.Builder builder = Matchers.FieldMatcher.newBuilder().withNameExact(name);
		if (!isForceAccess()) {
			builder.withModifierPublic();
		}

		List<IField> result = getFields(getSource(), instance, builder.build(), true);

		// No result
		if (result.isEmpty()) {
			throw new UnknownElementException("No such field in " + getSource().getName() + ": " + Util.fieldToString(getSource(), name, null));
		}

		// There may be more elements but these are ignored
		// The first field is the first field matched and thus the closest in the class hierarchy
		IField field = result.get(0);

		if (instance == null) {
			fieldCache.put(name, field);
		}

		return field;
	}

	/**
	 * Get a method by handle.
	 *
	 * @param name Method name.
	 * @param params Method parameter types.
	 * @return the method.
	 */
	public IMethod getMethod(String name, Class<?>... params) {
		return getMethod(name, null, params);
	}

	/**
	 * Get a method by handle.
	 *
	 * @param name Method name.
	 * @param instance Instance.
	 * @param params Method parameter types.
	 * @return the method.
	 */
	public IMethod getMethod(String name, Object instance, Class<?>... params) {
		// Preconditions
		if (name == null) {
			throw new NullPointerException("Name cannot be null");
		}
		if (params == null) {
			throw new NullPointerException("Parameters cannot be null");
		}
		for (int i = 0; i < params.length; i++) {
			if (params[i] == null) {
				throw new NullPointerException("Parameter cannot be null");
			}
		}

		// Cache
		Entry<String, Class<?>[]> key = Util.newTuple(name, params);
		if (instance == null && methodCache.containsKey(key)) {
			return methodCache.get(key);
		}

		// Query
		MethodQueryMatcher.Builder builder = Matchers.MethodMatcher.newBuilder().withNameExact(name).withParameters(params);
		if (!isForceAccess()) {
			builder.withModifierPublic();
		}

		List<IMethod> result = getMethods(getSource(), instance, builder.build(), true);

		// Remove all methods we override
		// This solution is not optimal
		List<IMethod> filtered = new ArrayList<>();
		for (IMethod method : result) {
			boolean match = false;
			for (IMethod filter : filtered) {
				if (Util.isSameHandle(method.getMethod(), filter.getMethod())) {
					match = true;
					break;
				}
			}

			if (!match) {
				filtered.add(method);
			}
		}

		// No result
		if (result.isEmpty()) {
			throw new UnknownElementException("No such method in " + getSource().getName() + ": " + Util.methodToString(getSource(), name, params));
		}

		// There may be more elements but these are ignored
		// The first field is the first field matched and thus the closest in the class hierarchy
		IMethod method = result.get(0);

		if (instance == null) {
			methodCache.put(key, method);
		}

		return method;
	}

	@Override
	public ExactReflectionHelper forced() {

		// Note that the cache is empty for new instances
		return isForceAccess() ? this : new ExactReflectionHelper(getSource(), true);
	}

}