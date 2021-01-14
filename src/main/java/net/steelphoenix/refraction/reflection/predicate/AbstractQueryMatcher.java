package net.steelphoenix.refraction.reflection.predicate;

import java.lang.reflect.Modifier;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import net.steelphoenix.refraction.member.IMember;

/**
 * A base matcher.
 * This matcher supports modifiers and name matching.
 *
 * @param <T> Matcher type.
 *
 * @author SteelPhoenix
 */
// TODO: Annotation matching
public abstract class AbstractQueryMatcher<T extends IMember> implements IQueryMatcher<T> {

	private final int modR;
	private final int modB;
	private final Predicate<String> name;
	private final Boolean synthetic;
	private final Predicate<T> predicate;

	protected AbstractQueryMatcher(int modR, int modB, Predicate<String> name, Boolean synthetic, Predicate<T> predicate) {
		this.modR = modR;
		this.modB = modB;
		this.name = name;
		this.synthetic = synthetic;
		this.predicate = predicate;
	}

	@Override
	public boolean matches(T object, Class<?> source, Object instance) {
		// Preconditions
		if (object == null) {
			throw new NullPointerException("Object cannot be null");
		}
		if (source == null) {
			throw new NullPointerException("Source cannot be null");
		}

		int mod = object.getModifiers();

		// Does not have the required modifiers
		if ((mod & modR) != modR) {
			return false;
		}

		// Does have banned modifiers
		if ((mod & modB) != 0) {
			return false;
		}

		// Does not match name
		if (name != null && !name.test(object.getName())) {
			return false;
		}

		// Does not match syntheticity
		if (synthetic != null && synthetic != object.isSynthetic()) {
			return false;
		}

		// Does not match custom predicate
		if (predicate != null && !predicate.test(object)) {
			return false;
		}

		return true;
	}

	/**
	 * A query matcher builder.
	 * This builder supports modifiers and name matching.
	 *
	 * @param <T> Matcher type.
	 *
	 * @author SteelPhoenix
	 */
	protected static abstract class Builder<T extends IMember> {

		protected int modR = 0;
		protected int modB = 0;
		protected Predicate<String> name = null;
		protected Boolean synthetic = null;
		protected Predicate<T> predicate = null;

		protected Builder() {
			// Nothing
		}

		/**
		 * Require the matched member to be publicly accessible.
		 *
		 * @return this for chaining.
		 */
		public Builder<T> withModifierPublic() {
			return withModifier(Modifier.PUBLIC);
		}

		/**
		 * Require the matched member to be statically accessible.
		 *
		 * @return this for chaining.
		 */
		public Builder<T> withModifierStatic() {
			return withModifier(Modifier.STATIC);
		}

		/**
		 * Require the matched member to have a given modifier.
		 *
		 * @param modifier Modifier to require.
		 * @return this for chaining.
		 *
		 * @see #withModifierPublic()
		 * @see #withModifierStatic()
		 * @see Modifier
		 */
		public Builder<T> withModifier(int modifier) {
			this.modR |= modifier;
			this.modB &= ~modifier;
			return this;
		}

		/**
		 * Require the matched member to not be final.
		 *
		 * @return this for chaining.
		 */
		public Builder<T> withoutModifierFinal() {
			return withoutModifier(Modifier.FINAL);
		}

		/**
		 * Require the matched member to not be statically accessible.
		 *
		 * @return this for chaining.
		 */
		public Builder<T> withoutModifierStatic() {
			return withoutModifier(Modifier.STATIC);
		}

		/**
		 * Require the matched member to not have a given modifier.
		 *
		 * @param modifier Modifier to require.
		 * @return this for chaining.
		 *
		 * @see #withoutModifierFinal()
		 * @see #withoutModifierStatic()
		 */
		public Builder<T> withoutModifier(int modifier) {
			this.modR &= ~modifier;
			this.modB |= modifier;
			return this;
		}

		/**
		 * Require the matched member name to have this name (case sensitive).
		 *
		 * @param text Required name.
		 * @return this for chaining.
		 */
		public Builder<T> withNameExact(String text) {
			return withNameRegex(text == null ? null : Pattern.quote(text));
		}

		/**
		 * Require the matched member name to match this regular expression.
		 *
		 * @param regex Regular expression.
		 * @return this for chaining.
		 */
		public Builder<T> withNameRegex(String regex) {
			return withNameRegex(regex == null ? null : Pattern.compile(regex));
		}

		/**
		 * Require the matched member name to match this pattern.
		 *
		 * @param pattern Pattern to use.
		 * @return this for chaining.
		 */
		public Builder<T> withNameRegex(Pattern pattern) {
			// Pattern#asPredicate() does not match the entire region
			return withName(pattern == null ? null : string -> pattern.matcher(string).matches());
		}

		/**
		 * Require the matched member name to match this predicate.
		 *
		 * @param predicate Predicate to use.
		 * @return this for chaining.
		 */
		public Builder<T> withName(Predicate<String> predicate) {
			this.name = predicate;
			return this;
		}

		/**
		 * Require the matched member to be synthetic or not.
		 *
		 * @param synthetic Syntheticity.
		 * @return this for chainint.
		 */
		public Builder<T> withSyntheticity(Boolean synthetic) {
			this.synthetic = synthetic;
			return this;
		}

		/**
		 * Use a custom predicate.
		 *
		 * @param predicate Predicate.
		 * @return this for chaining.
		 *
		 * @deprecated Please use dedicated methods whenever possible.
		 */
		@Deprecated
		public Builder<T> withPredicate(Predicate<T> predicate) {
			this.predicate = predicate;
			return this;
		}

		/**
		 * Build the matcher.
		 *
		 * @return the matcher.
		 */
		public abstract IQueryMatcher<T> build();
	}
}