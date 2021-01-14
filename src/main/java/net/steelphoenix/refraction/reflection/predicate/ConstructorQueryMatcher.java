package net.steelphoenix.refraction.reflection.predicate;

import java.lang.reflect.Array;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import net.steelphoenix.refraction.member.IConstructor;
import net.steelphoenix.refraction.member.IType;
import net.steelphoenix.refraction.member.Members;

/**
 * A constructor matcher.
 *
 * @author SteelPhoenix
 */
public class ConstructorQueryMatcher extends AbstractQueryMatcher<IConstructor> {

	private final IQueryMatcher<IType>[] paramMatcher;
	private final IQueryMatcher<IType>[] throwableMatcher;

	private ConstructorQueryMatcher(int modR, int modB, Predicate<String> name, Boolean synthetic, Predicate<IConstructor> predicate, IQueryMatcher<IType>[] paramMatcher, IQueryMatcher<IType>[] throwableMatcher) {
		super(modR, modB, name, synthetic, predicate);

		this.paramMatcher = paramMatcher == null || paramMatcher.length == 0 ? null : paramMatcher.clone();
		this.throwableMatcher = throwableMatcher == null || paramMatcher.length == 0 ? null : throwableMatcher.clone();
	}

	@Override
	public boolean matches(IConstructor object, Class<?> source, Object instance) {
		// Preconditions
		if (object == null) {
			throw new NullPointerException("Object cannot be null");
		}
		if (source == null) {
			throw new NullPointerException("Source cannot be null");
		}

		// No parent match
		if (!super.matches(object, source, instance)) {
			return false;
		}

		// Incorrect parameters
		if (paramMatcher != null) {
			Class<?>[] params = object.getConstructor().getParameterTypes();

			// Incorrect parameter count
			if (paramMatcher.length != params.length) {
				return false;
			}

			// For each parameter
			for (int i = 0; i < params.length; i++) {
				if (paramMatcher[i] != null && !paramMatcher[i].matches(Members.wrap(params[i]), source, instance)) {
					return false;
				}
			}
		}

		// Incorrect throwables
		if (throwableMatcher != null) {
			Class<?>[] thrown = object.getConstructor().getExceptionTypes();

			// Incorrect parameter count
			if (throwableMatcher.length != thrown.length) {
				return false;
			}

			// For each throwable
			for (int i = 0; i < thrown.length; i++) {
				if (throwableMatcher[i] != null && !throwableMatcher[i].matches(Members.wrap(thrown[i]), source, instance)) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Create a new builder instance.
	 *
	 * @return a builder to use.
	 */
	public static Builder newBuilder() {
		return new Builder();
	}

	/**
	 * A constructor matcher builder.
	 *
	 * @author SteelPhoenix
	 */
	public static class Builder extends AbstractQueryMatcher.Builder<IConstructor> {

		private IQueryMatcher<IType>[] paramMatcher = null;
		private IQueryMatcher<IType>[] throwableMatcher = null;

		private Builder() {
			// Nothing
		}

		@Override
		public Builder withModifierPublic() {
			return (Builder) super.withModifierPublic();
		}

		@Override
		public Builder withModifierStatic() {
			return (Builder) super.withModifierStatic();
		}

		@Override
		public Builder withModifier(int modifier) {
			return (Builder) super.withModifier(modifier);
		}

		@Override
		public Builder withoutModifierFinal() {
			return (Builder) super.withoutModifierFinal();
		}

		@Override
		public Builder withoutModifierStatic() {
			return (Builder) super.withoutModifierStatic();
		}

		@Override
		public Builder withoutModifier(int modifier) {
			return (Builder) super.withoutModifier(modifier);
		}

		@Override
		public Builder withNameExact(String text) {
			return (Builder) super.withNameExact(text);
		}

		@Override
		public Builder withNameRegex(String regex) {
			return (Builder) super.withNameRegex(regex);
		}

		@Override
		public Builder withNameRegex(Pattern pattern) {
			return (Builder) super.withNameRegex(pattern);
		}

		@Override
		public Builder withName(Predicate<String> predicate) {
			return (Builder) super.withName(predicate);
		}

		@Override
		public Builder withSyntheticity(Boolean synthetic) {
			return (Builder) super.withSyntheticity(synthetic);
		}

		@Deprecated
		@Override
		public Builder withPredicate(Predicate<IConstructor> predicate) {
			return (Builder) super.withPredicate(predicate);
		}

		/**
		 * Require the matched member to have no parameters.
		 *
		 * @return this for chaining.
		 */
		public Builder withParametersNone() {
			return withParameters(new Class<?>[0]);
		}

		/**
		 * Require the matched member to have these parameters types.
		 *
		 * @param parameters Parameter classes.
		 * @return this for chaining.
		 */
		@SuppressWarnings("unchecked")
		public Builder withParameters(Class<?>... parameters) {
			if (parameters == null) {
				return withParameters((IQueryMatcher<IType>[]) null);
			}

			// Wrap array
			IQueryMatcher<IType>[] matchers = (IQueryMatcher<IType>[]) Array.newInstance(IQueryMatcher.class, parameters.length);
			for (int i = 0; i < parameters.length; i++) {
				matchers[i] = parameters[i] == null ? null : Matchers.ClassMatcher.matchTypeExact(parameters[i]);
			}

			return withParameters(matchers);
		}

		/**
		 * Require the matched member to have parameters matching the given matchers.
		 *
		 * @param matchers Class matchers.
		 * @return this for chaining.
		 */
		@SuppressWarnings("unchecked") // I do not think this will be a problem when used correctly
		public Builder withParameters(IQueryMatcher<IType>... matchers) {
			this.paramMatcher = matchers == null || matchers.length == 0 ? null : matchers.clone();
			return this;
		}

		/**
		 * Require the matched member to have no throwables.
		 *
		 * @return this for chaining.
		 */
		public Builder withThrowablesNone() {
			return withThrowables(new Class<?>[0]);
		}

		/**
		 * Require the matched member to have these throwable types.
		 *
		 * @param throwables Throwable classes.
		 * @return this for chaining.
		 */
		@SuppressWarnings("unchecked")
		public Builder withThrowables(Class<?>... throwables) {
			if (throwables == null) {
				return withThrowables((IQueryMatcher<IType>[]) null);
			}

			// Wrap array
			IQueryMatcher<IType>[] matchers = (IQueryMatcher<IType>[]) Array.newInstance(IQueryMatcher.class, throwables.length);
			for (int i = 0; i < throwables.length; i++) {
				matchers[i] = throwables[i] == null ? null : Matchers.ClassMatcher.matchTypeExact(throwables[i]);
			}

			return withThrowables(matchers);
		}

		/**
		 * Require the matched member to have throwables matching the given matchers.
		 *
		 * @param matchers Class matchers.
		 * @return this for chaining.
		 */
		@SuppressWarnings("unchecked") // I do not think this will be a problem when used correctly
		public Builder withThrowables(IQueryMatcher<IType>... matchers) {
			this.throwableMatcher = matchers == null || matchers.length == 0 ? null : matchers.clone();
			return this;
		}

		@Override
		public IQueryMatcher<IConstructor> build() {
			return new ConstructorQueryMatcher(modR, modB, name, synthetic, predicate, paramMatcher, throwableMatcher);
		}
	}
}
