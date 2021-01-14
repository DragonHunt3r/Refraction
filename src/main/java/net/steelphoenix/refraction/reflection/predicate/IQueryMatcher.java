package net.steelphoenix.refraction.reflection.predicate;

import net.steelphoenix.refraction.member.IMember;

/**
 * A member predicate.
 *
 * @param <T> Predicate type.
 *
 * @author SteelPhoenix
 */
@FunctionalInterface
public interface IQueryMatcher<T extends IMember> {

	/**
	 * Test a given object.
	 *
	 * @param object Object to test.
	 * @param source Source class for the object.
	 * @param instance Working instance.
	 * @return if this object matches the predicate.
	 */
	public boolean matches(T object, Class<?> source, Object instance);

	/**
	 * Get an inverted version of this predicate.
	 *
	 * @return the modified matcher.
	 */
	public default IQueryMatcher<T> negated() {
		return (object, source, instance) -> !matches(object, source, instance);
	}

	/**
	 * Get a version of this predicate where a given predicate also needs to match.
	 *
	 * @param matcher Predicate.
	 * @return the modified matcher.
	 */
	public default IQueryMatcher<T> and(IQueryMatcher<T> matcher) {
		// Preconditions
		if (matcher == null) {
			throw new NullPointerException("Matcher cannot be null");
		}

		return (object, source, instance) -> matches(object, source, instance) && matcher.matches(object, source, instance);
	}

	/**
	 * Get a version of this predicate where a given predicate needs to match if this predicate does not match.
	 *
	 * @param matcher Predicate.
	 * @return the modified matcher.
	 */
	public default IQueryMatcher<T> or(IQueryMatcher<T> matcher) {
		// Preconditions
		if (matcher == null) {
			throw new NullPointerException("Matcher cannot be null");
		}

		return (object, source, instance) -> matches(object, source, instance) || matcher.matches(object, source, instance);
	}
}