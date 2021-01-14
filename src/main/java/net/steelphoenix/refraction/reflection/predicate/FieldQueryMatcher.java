package net.steelphoenix.refraction.reflection.predicate;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import net.steelphoenix.refraction.member.IField;
import net.steelphoenix.refraction.member.IType;
import net.steelphoenix.refraction.member.Members;

/**
 * A field matcher.
 *
 * @author SteelPhoenix
 */
public class FieldQueryMatcher extends AbstractQueryMatcher<IField> {

	private final IQueryMatcher<IType> typeMatcher;

	private FieldQueryMatcher(int modR, int modB, Predicate<String> name, Boolean synthetic, Predicate<IField> predicate, IQueryMatcher<IType> typeMatcher) {
		super(modR, modB, name, synthetic, predicate);

		this.typeMatcher = typeMatcher;
	}

	@Override
	public boolean matches(IField object, Class<?> source, Object instance) {
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

		// Incorrect type
		if (typeMatcher != null && !typeMatcher.matches(Members.wrap(object.getField().getType()), source, instance)) {
			return false;
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
	 * A field matcher builder.
	 *
	 * @author SteelPhoenix
	 */
	public static class Builder extends AbstractQueryMatcher.Builder<IField> {

		private int modR = 0;
		private int modB = 0;
		private Predicate<String> name = null;
		private IQueryMatcher<IType> typeMatcher = null;

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
		public Builder withPredicate(Predicate<IField> predicate) {
			return (Builder) super.withPredicate(predicate);
		}

		/**
		 * Require the matched member to have this type.
		 *
		 * @param type Class type.
		 * @return this for chaining.
		 */
		public Builder withType(Class<?> type) {
			return withType(type == null ? null : Matchers.ClassMatcher.matchTypeExact(type));
		}

		/**
		 * Require the matched member to have a type matching this matcher.
		 *
		 * @param matcher Class matcher or null for no matching.
		 * @return this for chaining.
		 */
		public Builder withType(IQueryMatcher<IType> matcher) {
			this.typeMatcher = matcher;
			return this;
		}

		@Override
		public IQueryMatcher<IField> build() {
			return new FieldQueryMatcher(modR, modB, name, synthetic, predicate, typeMatcher);
		}
	}
}
