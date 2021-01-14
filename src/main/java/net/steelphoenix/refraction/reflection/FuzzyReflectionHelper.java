package net.steelphoenix.refraction.reflection;

import java.util.ArrayList;
import java.util.List;

import net.steelphoenix.refraction.Util;
import net.steelphoenix.refraction.member.IConstructor;
import net.steelphoenix.refraction.member.IField;
import net.steelphoenix.refraction.member.IMethod;
import net.steelphoenix.refraction.reflection.predicate.ConstructorQueryMatcher;
import net.steelphoenix.refraction.reflection.predicate.FieldQueryMatcher;
import net.steelphoenix.refraction.reflection.predicate.IQueryMatcher;
import net.steelphoenix.refraction.reflection.predicate.Matchers;
import net.steelphoenix.refraction.reflection.predicate.MethodQueryMatcher;


/**
 * A reflection helper for fuzzy member matching.
 *
 * @author SteelPhoenix
 */
public class FuzzyReflectionHelper extends ReflectionHelper {

	public FuzzyReflectionHelper(Class<?> clazz) {
		super(clazz);
	}

	public FuzzyReflectionHelper(Class<?> clazz, boolean force) {
		super(clazz, force);
	}

	/**
	 * Get constructors by parameter types.
	 *
	 * @param params Constructor parameter types.
	 * @return the constructors.
	 */
	public List<IConstructor> getConstructors(Class<?>... params) {
		// Query
		ConstructorQueryMatcher.Builder builder = Matchers.ConstructorMatcher.newBuilder().withParameters(params);
		if (!isForceAccess()) {
			builder.withModifierPublic();
		}

		return getConstructors(builder.build(), false);
	}

	/**
	 * Get constructors matching a given matcher.
	 *
	 * @param matcher Matcher.
	 * @param recursive If superclasses should get searched.
	 * @return the constructors.
	 */
	public List<IConstructor> getConstructors(IQueryMatcher<IConstructor> matcher, boolean recursive) {
		return getConstructors(getSource(), null, matcher, recursive);
	}

	/**
	 * Get fields by type.
	 *
	 * @param type Field type.
	 * @return the fields.
	 */
	public List<IField> getFields(Class<?> type) {
		return getFields(type, null);
	}

	/**
	 * Get fields by type.
	 *
	 * @param type Field type.
	 * @param instance Instance.
	 * @return the fields.
	 */
	public List<IField> getFields(Class<?> type, Object instance) {

		// Query
		FieldQueryMatcher.Builder builder = Matchers.FieldMatcher.newBuilder().withType(type);
		if (!isForceAccess()) {
			builder.withModifierPublic();
		}

		return getFields(getSource(), instance, builder.build(), true);
	}

	/**
	 * Get fields matching a given matcher.
	 *
	 * @param matcher Matcher.
	 * @param recursive If superclasses should get searched.
	 * @return the fields.
	 */
	public List<IField> getFields(IQueryMatcher<IField> matcher, boolean recursive) {
		return getFields(getSource(), null, matcher, recursive);
	}

	/**
	 * Get methods by parameter types.
	 *
	 * @param params Method parameter types.
	 * @return the methods.
	 */
	public List<IMethod> getMethods(Class<?>... params) {
		return getMethods(null, params);
	}

	/**
	 * Get methods by parameter types.
	 *
	 * @param instance Instance.
	 * @param params Method parameter types.
	 * @return the methods.
	 */
	public List<IMethod> getMethods(Object instance, Class<?>... params) {

		// Query
		MethodQueryMatcher.Builder builder = Matchers.MethodMatcher.newBuilder().withParameters(params);
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

		return filtered;
	}

	/**
	 * Get methods matching a given matcher.
	 *
	 * @param matcher Matcher.
	 * @param recursive If superclasses should get searched.
	 * @return the methods.
	 */
	public List<IMethod> getMethods(IQueryMatcher<IMethod> matcher, boolean recursive) {
		List<IMethod> result = getMethods(getSource(), null, matcher, recursive);

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

		return filtered;
	}

	@Override
	public FuzzyReflectionHelper forced() {
		return isForceAccess() ? this : new FuzzyReflectionHelper(getSource(), true);
	}
}
