package net.steelphoenix.refraction.reflection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import net.steelphoenix.refraction.member.IConstructor;
import net.steelphoenix.refraction.member.IField;
import net.steelphoenix.refraction.member.IMember;
import net.steelphoenix.refraction.member.IMethod;
import net.steelphoenix.refraction.member.Members;
import net.steelphoenix.refraction.reflection.predicate.IQueryMatcher;

/**
 * A reflection helper.
 *
 * @author SteelPhoenix
 */
public abstract class ReflectionHelper {

	private final Class<?> clazz;
	private final boolean force;

	protected ReflectionHelper(Class<?> clazz) {
		this(clazz, false);
	}

	protected ReflectionHelper(Class<?> clazz, boolean force) {
		if (clazz == null) {
			throw new NullPointerException("Class cannot be null");
		}

		this.clazz = clazz;
		this.force = force;
	}

	/**
	 * Get the source class we are searching.
	 *
	 * @return the source class.
	 */
	public Class<?> getSource() {
		return clazz;
	}

	/**
	 * Get if we are including scope restricted members.
	 *
	 * @return if we look at all members no matter the visibility.
	 */
	public boolean isForceAccess() {
		return force;
	}

	/**
	 * Get a helper instance where scope restrictions are ignored.
	 *
	 * @return a version of the current helper ignoring scope restrictions.
	 *
	 * @see #isForceAccess()
	 */
	public abstract ReflectionHelper forced();


	/**
	 * Get an exact reflection helper from a given type.
	 *
	 * @param type Type to use.
	 * @return the reflection helper.
	 */
	public static ExactReflectionHelper fromClassExact(Class<?> type) {
		// Preconditions
		if (type == null) {
			throw new NullPointerException("Type cannot be null");
		}

		return new ExactReflectionHelper(type);
	}

	/**
	 * Get an exact reflection helper from a given type.
	 *
	 * @param type Type to use.
	 * @param force If scope restrictions are ignored.
	 * @return the reflection helper.
	 */
	public static ExactReflectionHelper fromClassExact(Class<?> type, boolean force) {
		// Preconditions
		if (type == null) {
			throw new NullPointerException("Type cannot be null");
		}

		return new ExactReflectionHelper(type, force);
	}

	/**
	 * Get an exact reflection helper from a given object.
	 *
	 * @param object Object to use.
	 * @return the reflection helper.
	 */
	public static ExactReflectionHelper fromObjectExact(Object object) {
		// Preconditions
		if (object == null) {
			throw new NullPointerException("Object cannot be null");
		}

		return fromClassExact(object.getClass());
	}

	/**
	 * Get an exact reflection helper from a given object.
	 *
	 * @param object Object to use.
	 * @param force If scope restrictions are ignored.
	 * @return the reflection helper.
	 */
	public static ExactReflectionHelper fromObjectExact(Object object, boolean force) {
		// Preconditions
		if (object == null) {
			throw new NullPointerException("Object cannot be null");
		}

		return fromClassExact(object.getClass(), force);
	}

	/**
	 * Get a fuzzy reflection helper from a given type.
	 *
	 * @param type Type to use.
	 * @return the reflection helper.
	 */
	public static FuzzyReflectionHelper fromClassFuzzy(Class<?> type) {
		// Preconditions
		if (type == null) {
			throw new NullPointerException("Type cannot be null");
		}

		return new FuzzyReflectionHelper(type);
	}

	/**
	 * Get a fuzzy reflection helper from a given type.
	 *
	 * @param type Type to use.
	 * @param force If scope restrictions are ignored.
	 * @return the reflection helper.
	 */
	public static FuzzyReflectionHelper fromClassFuzzy(Class<?> type, boolean force) {
		// Preconditions
		if (type == null) {
			throw new NullPointerException("Type cannot be null");
		}

		return new FuzzyReflectionHelper(type, force);
	}

	/**
	 * Get a fuzzy reflection helper from a given object.
	 *
	 * @param object Object to use.
	 * @return the reflection helper.
	 */
	public static FuzzyReflectionHelper fromObjectFuzzy(Object object) {
		// Preconditions
		if (object == null) {
			throw new NullPointerException("Object cannot be null");
		}

		return fromClassFuzzy(object.getClass());
	}

	/**
	 * Get a fuzzy reflection helper from a given object.
	 *
	 * @param object Object to use.
	 * @param force If scope restrictions are ignored.
	 * @return the reflection helper.
	 */
	public static FuzzyReflectionHelper fromObjectFuzzy(Object object, boolean force) {
		// Preconditions
		if (object == null) {
			throw new NullPointerException("Object cannot be null");
		}

		return fromClassFuzzy(object.getClass(), force);
	}

	/**
	 * Get all constructors matching a given predicate.
	 *
	 * @param type Class to look at.
	 * @param instance Working instance.
	 * @param matcher Constructor predicate.
	 * @param recursive If superclasses should get searched.
	 * @return a list of matching constructors.
	 */
	public static List<IConstructor> getConstructors(Class<?> type, Object instance, IQueryMatcher<IConstructor> matcher, boolean recursive) {
		return query(type, instance, matcher, recursive, clazz -> Arrays.stream(clazz.getDeclaredConstructors()).map(Members::wrap).toArray(IConstructor[]::new));
	}

	/**
	 * Get all fields matching a given predicate.
	 *
	 * @param type Class to look at.
	 * @param instance Working instance.
	 * @param matcher Field predicate.
	 * @param recursive If superclasses should get searched.
	 * @return a list of matching fields.
	 */
	public static List<IField> getFields(Class<?> type, Object instance, IQueryMatcher<IField> matcher, boolean recursive) {
		return query(type, instance, matcher, recursive, clazz -> Arrays.stream(clazz.getDeclaredFields()).map(Members::wrap).toArray(IField[]::new));
	}

	/**
	 * Get all methods matching a given predicate.
	 *
	 * @param type Class to look at.
	 * @param instance Working instance.
	 * @param matcher Method predicate.
	 * @param recursive If superclasses should get searched.
	 * @return a list of matching methods.
	 */
	public static List<IMethod> getMethods(Class<?> type, Object instance, IQueryMatcher<IMethod> matcher, boolean recursive) {
		return query(type, instance, matcher, recursive, clazz -> Arrays.stream(clazz.getDeclaredMethods()).map(Members::wrap).toArray(IMethod[]::new));
	}

	/**
	 * Query members.
	 *
	 * @param <T> Member type.
	 * @param type Class to look at.
	 * @param instance Working instance.
	 * @param matcher Member predicate.
	 * @param recursive If superclasses should get searched.
	 * @param provider Member provider.
	 * @return a list of matching members.
	 */
	private static <T extends IMember> List<T> query(Class<?> type, Object instance, IQueryMatcher<T> matcher, boolean recursive, Function<Class<?>, T[]> provider) {
		// Preconditions
		if (type == null) {
			throw new NullPointerException("Type cannot be null");
		}
		if (provider == null) {
			throw new NullPointerException("Provider cannot be null");
		}

		List<T> members = new ArrayList<>();
		for (T t : provider.apply(type)) {
			if (matcher == null || matcher.matches(t, type, instance)) {
				members.add(t);
			}
		}

		// No more superclasses to search
		if (type.getSuperclass() == null) {
			return members;
		}

		// We do not match inherited fields so this should not cause issues
		if (recursive) {
			members.addAll(query(type.getSuperclass(), instance, matcher, recursive, provider));
		}
		return members;
	}
}
