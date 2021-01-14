package net.steelphoenix.refraction.reflection.predicate;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.steelphoenix.refraction.Util;
import net.steelphoenix.refraction.member.IConstructor;
import net.steelphoenix.refraction.member.IField;
import net.steelphoenix.refraction.member.IMember;
import net.steelphoenix.refraction.member.IMethod;
import net.steelphoenix.refraction.member.IType;
import net.steelphoenix.refraction.member.Members;

/**
 * A class matcher.
 *
 * @author SteelPhoenix
 */
public class ClassQueryMatcher extends AbstractQueryMatcher<IType> {

	private final Set<Class<?>> classes;
	private final boolean array;
	private final IQueryMatcher<IType> arrayMatcher;
	private final Class<?> parent;
	private final Class<?> child;
	private final Set<Entry<IQueryMatcher<IType>, MemberMatcherOption[]>> classMatcher;
	private final Set<Entry<IQueryMatcher<IConstructor>, MemberMatcherOption[]>> constructorMatcher;
	private final Set<Entry<IQueryMatcher<IField>, MemberMatcherOption[]>> fieldMatcher;
	private final Set<Entry<IQueryMatcher<IMethod>, MemberMatcherOption[]>> methodMatcher;

	private ClassQueryMatcher(int modR, int modB, Predicate<String> name, Boolean synthetic, Predicate<IType> predicate, Set<Class<?>> classes, boolean array, IQueryMatcher<IType> arrayMatcher, Class<?> parent, Class<?> child, Set<Entry<IQueryMatcher<IType>, MemberMatcherOption[]>> classMatcher, Set<Entry<IQueryMatcher<IConstructor>, MemberMatcherOption[]>> constructorMatcher, Set<Entry<IQueryMatcher<IField>, MemberMatcherOption[]>> fieldMatcher, Set<Entry<IQueryMatcher<IMethod>, MemberMatcherOption[]>> methodMatcher) {
		super(modR, modB, name, synthetic, predicate);

		this.classes = classes == null || classes.isEmpty() ? null : new HashSet<>(classes);
		this.array = array;
		this.arrayMatcher = arrayMatcher;
		this.parent = parent;
		this.child = child;
		this.classMatcher = sanitize(classMatcher);
		this.constructorMatcher = sanitize(constructorMatcher);
		this.fieldMatcher = sanitize(fieldMatcher);
		this.methodMatcher = sanitize(methodMatcher);
	}

	@Override
	public boolean matches(IType object, Class<?> source, Object instance) {
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

		// Not correct type
		if (classes != null && !classes.contains(object.getType())) {
			return false;
		}

		// Not correct array
		if (array && (!object.isArray() || (arrayMatcher != null && !arrayMatcher.matches(Members.wrap(object.getType().getComponentType()), source, instance)))) {
			return false;
		}

		// Not correct superclass
		if (parent != null && !Members.wrap(parent).isSuperClassOf(object.getType())) {
			return false;
		}

		// Not correct child class
		if (child != null && !Members.wrap(child).isSubClassOf(object.getType())) {
			return false;
		}

		// Class matchers
		if (!memberMatcher(source, instance, classMatcher, object.getTypes())) {
			return false;
		}

		// Constructor matchers
		if (!memberMatcher(source, instance, constructorMatcher, object.getConstructors())) {
			return false;
		}

		// Field matchers
		if (!memberMatcher(source, instance, fieldMatcher, object.getFields())) {
			return false;
		}

		// Method matchers
		if (!memberMatcher(source, instance, methodMatcher, object.getMethods())) {
			return false;
		}

		return true;
	}

	/**
	 * Match members.
	 *
	 * @param <T> Member type.
	 * @param source Source class for the object.
	 * @param instance Working instance.
	 * @param matchers Matcher entries.
	 * @param collection Members.
	 * @return if the members match the predicates.
	 */
	private <T extends IMember> boolean memberMatcher(Class<?> source, Object instance, Set<Entry<IQueryMatcher<T>, MemberMatcherOption[]>> matchers, Collection<T> collection) {
		// Preconditions
		if (source == null) {
			throw new NullPointerException("Source cannot be null");
		}
		if (collection == null) {
			throw new NullPointerException("Members cannot be null");
		}

		// No predicates
		if (matchers == null || matchers.isEmpty()) {
			return true;
		}

		// Count matches
		int i = 0;
		int[] matches = new int[collection.size()];
		boolean[] unique = new boolean[matchers.size()];
		for (Entry<IQueryMatcher<T>, MemberMatcherOption[]> entry : matchers) {
			int count = 0;
			int j = 0;

			// Match count
			for (T t : collection) {
				if (entry.getKey() == null || entry.getKey().matches(t, source, instance)) {
					count++;
					matches[j]++;
				}
				j++;
			}

			// Options
			if (entry.getValue() != null) {
				for (MemberMatcherOption option : entry.getValue()) {

					// We cannot process this yet
					if (option == MemberMatcherOption.MATCH_UNIQUE) {
						unique[i] = true;
						continue;
					}

					// Everything matches
					if (option == MemberMatcherOption.MATCH_ALL) {
						if (count != collection.size()) {
							return false;
						}
						continue;
					}

					if (option != null) {
						// Range check
						Entry<Integer, Integer> range = option.getRange();
						if (count < range.getKey() || count > range.getValue()) {
							return false;
						}
					}
				}
			}

			i++;
		}

		// Unfortunately we need a second loop to check unique matches
		i = 0;
		for (Entry<IQueryMatcher<T>, MemberMatcherOption[]> entry : matchers) {
			if (unique[i]) {
				int j = 0;
				for (T t : collection) {
					if (entry.getKey() == null || entry.getKey().matches(t, source, instance)) {
						if (matches[j] != 1) {
							return false;
						}
					}
					j++;
				}
			}

			i++;
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
	 * Sanitize a matcher set for use.
	 *
	 * @param <T> Matcher type.
	 * @param set Matcher set.
	 * @return a sanitized set.
	 */
	private static <T extends IMember> Set<Entry<IQueryMatcher<T>, MemberMatcherOption[]>> sanitize(Set<Entry<IQueryMatcher<T>, MemberMatcherOption[]>> set) {
		if (set == null || set.isEmpty()) {
			return null;
		}

		// Create new tuples
		return set.stream().map(entry -> Util.newTuple(entry.getKey(), entry.getValue())).collect(Collectors.toSet());
	}

	/**
	 * Member matcher options.
	 *
	 * @author SteelPhoenix
	 */
	public static class MemberMatcherOption {

		/**
		 * A member matcher where the matched member must be matched by this matcher only.
		 */
		public static final MemberMatcherOption MATCH_UNIQUE = new MemberMatcherOption();

		/**
		 * A member matcher where the matcher must match all members.
		 */
		public static final MemberMatcherOption MATCH_ALL = new MemberMatcherOption();

		/**
		 * A member matcher where the matcher must match at least one member.
		 */
		public static final MemberMatcherOption MATCH_ANY = matchAtLeast(1);

		/**
		 * A member matcher where the matcher must match exactly one member.
		 */
		public static final MemberMatcherOption MATCH_ONE = matchExactly(1);

		private final int min;
		private final int max;

		private MemberMatcherOption() {
			this(0, 0);
		}

		private MemberMatcherOption(int min, int max) {
			if (min < 0) {
				throw new IllegalArgumentException("Minimum cannot be negative");
			}
			if (max < 0) {
				throw new IllegalArgumentException("Maximum cannot be negative");
			}
			if (min > max) {
				throw new IllegalArgumentException("Minimum cannot exceed maximum");
			}

			this.min = min;
			this.max = max;
		}

		/**
		 * Get the match range.
		 *
		 * @return the range.
		 */
		private Entry<Integer, Integer> getRange() {
			return Util.newTuple(min, max);
		}

		/**
		 * A member matcher where the matcher must match an exact amount of members.
		 *
		 * @param count Match count.
		 * @return the matcher option.
		 */
		public static MemberMatcherOption matchExactly(int count) {
			return matchInRange(count, count);
		}

		/**
		 * A member matcher where the matches must match at least the given amount.
		 *
		 * @param count Match count.
		 * @return the matcher option.
		 */
		public static MemberMatcherOption matchAtLeast(int count) {
			return matchInRange(count, Integer.MAX_VALUE);
		}

		/**
		 * A member matcher where the matches must match at most the given amount.
		 *
		 * @param count Match count.
		 * @return the matcher option.
		 */
		public static MemberMatcherOption matchAtMost(int count) {
			return matchInRange(0, count);
		}

		/**
		 * A member matcher where the matches must match a range of members.
		 *
		 * @param min Minimum match count.
		 * @param max Maximum match count.
		 * @return the matcher option.
		 */
		public static MemberMatcherOption matchInRange(int min, int max) {
			// Preconditions
			if (min < 0) {
				throw new IllegalArgumentException("Minimum cannot be negative");
			}
			if (max < 0) {
				throw new IllegalArgumentException("Maximum cannot be negative");
			}
			if (min > max) {
				throw new IllegalArgumentException("Minimum cannot exceed maximum");
			}

			return new MemberMatcherOption(min, max);
		}
	}

	/**
	 * A class matcher builder.
	 *
	 * @author SteelPhoenix
	 */
	public static class Builder extends AbstractQueryMatcher.Builder<IType> {

		private final Set<Class<?>> classes = new HashSet<>();
		private final Set<Entry<IQueryMatcher<IType>, MemberMatcherOption[]>> classMatcher = new HashSet<>();
		private final Set<Entry<IQueryMatcher<IConstructor>, MemberMatcherOption[]>> constructorMatcher = new HashSet<>();
		private final Set<Entry<IQueryMatcher<IField>, MemberMatcherOption[]>> fieldMatcher = new HashSet<>();
		private final Set<Entry<IQueryMatcher<IMethod>, MemberMatcherOption[]>> methodMatcher = new HashSet<>();
		private boolean array = false;
		private IQueryMatcher<IType> arrayMatcher = null;
		private Class<?> parent = null;
		private Class<?> child = null;

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
		public Builder withPredicate(Predicate<IType> predicate) {
			return (Builder) super.withPredicate(predicate);
		}

		/**
		 * Require the matched member to be one of these classes.
		 * Note that calling this method multiple times overrides all previous calls.
		 *
		 * @param classes Matchable classes.
		 * @return this for chaining.
		 */
		public Builder withTypeExact(Class<?>... classes) {
			this.classes.clear();

			if (classes != null) {
				for (Class<?> clazz : classes) {
					this.classes.add(clazz);
				}
			}

			return this;
		}

		/**
		 * Require the matched member to be an array.
		 *
		 * @return this for chaining.
		 */
		public Builder withTypeArray() {
			return withTypeArray(null);
		}

		/**
		 * Require the matched member to be an array and the component type to match the given matcher.
		 *
		 * @param matcher Class matcher.
		 * @return this for chaining.
		 */
		public Builder withTypeArray(IQueryMatcher<IType> matcher) {
			this.array = true;
			this.arrayMatcher = matcher;
			return this;
		}

		/**
		 * Require the matched member to be a child class the given class.
		 *
		 * @param clazz Superclass.
		 * @return this for chaining.
		 */
		public Builder withSuperClass(Class<?> clazz) {
			this.parent = clazz;
			return this;
		}

		/**
		 * Require the matched member to be a superclass of the given class.
		 *
		 * @param clazz Child class.
		 * @return this for chaining.
		 */
		public Builder withSubClass(Class<?> clazz) {
			this.child = clazz;
			return this;
		}

		/**
		 * Require the matched member to have classes matching the given matcher and options.
		 * Note that this adds a filter, it does not replace set filters so multiple calls can be made to add multiple filters.
		 *
		 * @param matcher Member matcher.
		 * @param options Matcher options.
		 * @return this for chaining.
		 */
		public Builder withMemberClass(IQueryMatcher<IType> matcher, MemberMatcherOption... options) {
			return withMember(classMatcher, matcher, options);
		}

		/**
		 * Require the matched member to have constructors matching the given matcher and options.
		 * Note that this adds a filter, it does not replace set filters so multiple calls can be made to add multiple filters.
		 *
		 * @param matcher Member matcher.
		 * @param options Matcher options.
		 * @return this for chaining.
		 */
		public Builder withMemberConstructor(IQueryMatcher<IConstructor> matcher, MemberMatcherOption... options) {
			return withMember(constructorMatcher, matcher, options);
		}

		/**
		 * Require the matched member to have fields matching the given matcher and options.
		 * Note that this adds a filter, it does not replace set filters so multiple calls can be made to add multiple filters.
		 *
		 * @param matcher Member matcher.
		 * @param options Matcher options.
		 * @return this for chaining.
		 */
		public Builder withMemberField(IQueryMatcher<IField> matcher, MemberMatcherOption... options) {
			return withMember(fieldMatcher, matcher, options);
		}

		/**
		 * Require the matched member to have methods matching the given matcher and options.
		 * Note that this adds a filter, it does not replace set filters so multiple calls can be made to add multiple filters.
		 *
		 * @param matcher Member matcher.
		 * @param options Matcher options.
		 * @return this for chaining.
		 */
		public Builder withMemberMethod(IQueryMatcher<IMethod> matcher, MemberMatcherOption... options) {
			return withMember(methodMatcher, matcher, options);
		}

		@Override
		public IQueryMatcher<IType> build() {
			return new ClassQueryMatcher(modR, modB, name, synthetic, predicate, classes, array, arrayMatcher, parent, child, classMatcher, constructorMatcher, fieldMatcher, methodMatcher);
		}

		/**
		 * Require the matched member to have members matching the given matcher and options.
		 * Note that this adds a filter, it does not replace set filters so multiple calls can be made to add multiple filters.
		 *
		 * @param set Matcher set.
		 * @param matcher Member matcher.
		 * @param options Matcher options.
		 * @return this for chaining.
		 */
		private <T extends IMember> Builder withMember(Set<Entry<IQueryMatcher<T>, MemberMatcherOption[]>> set, IQueryMatcher<T> matcher, MemberMatcherOption[] options) {
			// Preconditions
			if (set == null) {
				throw new NullPointerException("Set cannot be null");
			}

			set.add(Util.newTuple(matcher, options == null || options.length == 0 ? null : Arrays.stream(options)
					.filter(option -> option != null)
					.toArray(MemberMatcherOption[]::new)));
			return this;
		}
	}
}
