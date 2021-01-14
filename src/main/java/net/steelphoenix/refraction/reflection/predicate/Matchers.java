package net.steelphoenix.refraction.reflection.predicate;

import net.steelphoenix.refraction.member.IConstructor;
import net.steelphoenix.refraction.member.IField;
import net.steelphoenix.refraction.member.IMethod;
import net.steelphoenix.refraction.member.IType;

/**
 * A utility class for query matchers.
 *
 * @author SteelPhoenix
 */
public class Matchers {

	private Matchers() {
		// Nothing
	}

	/**
	 * Class query matchers.
	 *
	 * @author SteelPhoenix
	 */
	public static class ClassMatcher {

		private ClassMatcher() {
			// Nothing
		}

		/**
		 * Get a new class matcher builder.
		 *
		 * @return the builder.
		 *
		 * @see ClassQueryMatcher#newBuilder()
		 */
		public static ClassQueryMatcher.Builder newBuilder() {
			return ClassQueryMatcher.newBuilder();
		}

		/**
		 * Get a matcher matching an exact class.
		 *
		 * @param clazz Class type.
		 * @return the matcher.
		 */
		public static IQueryMatcher<IType> matchTypeExact(Class<?> clazz) {
			return newBuilder().withTypeExact(clazz).build();
		}

		/**
		 * Get a matcher matching an exact name.
		 * Note that this includes package and declaring classes if available.
		 *
		 * @param name Class name.
		 * @return the matcher.
		 */
		public static IQueryMatcher<IType> matchNameExact(String name) {
			return newBuilder().withNameExact(name).build();
		}

		/**
		 * Get a matcher matching a name pattern.
		 *
		 * @param regex Class name pattern.
		 * @return the matcher.
		 */
		public static IQueryMatcher<IType> matchNameRegex(String regex) {
			return newBuilder().withNameRegex(regex).build();
		}
	}

	/**
	 * Constructor query matchers.
	 *
	 * @author SteelPhoenix
	 */
	public static class ConstructorMatcher {

		private ConstructorMatcher() {
			// Nothing
		}

		/**
		 * Get a new constructor matcher builder.
		 *
		 * @return the builder.
		 *
		 * @see ConstructorQueryMatcher#newBuilder()
		 */
		public static ConstructorQueryMatcher.Builder newBuilder() {
			return ConstructorQueryMatcher.newBuilder();
		}

		/**
		 * Get a matcher matching constructors without parameters.
		 *
		 * @return the matcher.
		 */
		public static IQueryMatcher<IConstructor> matchParametersNone() {
			return newBuilder().withParametersNone().build();
		}

		/**
		 * Get a matcher matching constructors with the given parameters types.
		 *
		 * @param params Parameter types.
		 * @return the matcher.
		 */
		public static IQueryMatcher<IConstructor> matchParameters(Class<?>... params) {
			return newBuilder().withParameters(params).build();
		}
	}

	/**
	 * Field query matchers.
	 *
	 * @author SteelPhoenix
	 */
	public static class FieldMatcher {

		private FieldMatcher() {
			// Nothing
		}

		/**
		 * Get a new method matcher builder.
		 *
		 * @return the builder.
		 *
		 * @see FieldQueryMatcher#newBuilder()
		 */
		public static FieldQueryMatcher.Builder newBuilder() {
			return FieldQueryMatcher.newBuilder();
		}

		/**
		 * Get a matcher matching an exact class.
		 *
		 * @param clazz Class type.
		 * @return the matcher.
		 */
		public static IQueryMatcher<IField> matchTypeExact(Class<?> clazz) {
			return newBuilder().withType(clazz).build();
		}

		/**
		 * Get a matcher matching an exact name.
		 *
		 * @param name Field name.
		 * @return the matcher.
		 */
		public static IQueryMatcher<IField> matchNameExact(String name) {
			return newBuilder().withNameExact(name).build();
		}

		/**
		 * Get a matcher matching a name pattern.
		 *
		 * @param regex Field name pattern.
		 * @return the matcher.
		 */
		public static IQueryMatcher<IField> matchNameRegex(String regex) {
			return newBuilder().withNameRegex(regex).build();
		}
	}

	/**
	 * Method query matchers.
	 *
	 * @author SteelPhoenix
	 */
	public static class MethodMatcher {

		private MethodMatcher() {
			// Nothing
		}

		/**
		 * Get a new method matcher builder.
		 *
		 * @return the builder.
		 *
		 * @see MethodQueryMatcher#newBuilder()
		 */
		public static MethodQueryMatcher.Builder newBuilder() {
			return MethodQueryMatcher.newBuilder();
		}

		/**
		 * Get a matcher matching an exact name.
		 * Note that this includes package and declaring classes if available.
		 *
		 * @param name Method name.
		 * @return the matcher.
		 */
		public static IQueryMatcher<IMethod> matchNameExact(String name) {
			return newBuilder().withNameExact(name).build();
		}

		/**
		 * Get a matcher matching a name pattern.
		 *
		 * @param regex Method name pattern.
		 * @return the matcher.
		 */
		public static IQueryMatcher<IMethod> matchNameRegex(String regex) {
			return newBuilder().withNameRegex(regex).build();
		}

		/**
		 * Get a matcher matching methods with the given return type.
		 *
		 * @param clazz Return type.
		 * @return the matcher.
		 */
		public static IQueryMatcher<IMethod> matchReturnType(Class<?> clazz) {
			return newBuilder().withReturnType(clazz).build();
		}

		/**
		 * Get a matcher matching methods with the given parameters types.
		 *
		 * @param params Parameter types.
		 * @return the matcher.
		 */
		public static IQueryMatcher<IMethod> matchParameters(Class<?>... params) {
			return newBuilder().withParameters(params).build();
		}
	}
}
