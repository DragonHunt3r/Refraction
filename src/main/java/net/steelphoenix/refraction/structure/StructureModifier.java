package net.steelphoenix.refraction.structure;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.steelphoenix.refraction.converter.IConverter;
import net.steelphoenix.refraction.member.IField;
import net.steelphoenix.refraction.reflection.ReflectionHelper;
import net.steelphoenix.refraction.reflection.predicate.Matchers;

/**
 * A structure modifier.
 * Note that all subtype modifiers are cached, and that static fields and fields from superclasses are also accessible.
 *
 * @param <T> Modifier type.
 *
 * @author SteelPhoenix
 */
public class StructureModifier<T> extends AbstractStructureModifier<T> {

	private static final Map<Class<?>, IStructureModifier<?>> CACHE = new ConcurrentHashMap<>();
	private List<IField> fields;
	private IConverter<T> converter;
	private Map<Class<?>, IStructureModifier<?>> cache;

	private StructureModifier(Class<?> targetType, Class<T> objectType, Class<?> fieldType) {
		super(targetType, objectType, fieldType);
	}

	@Override
	public IStructureModifier<T> withTarget(Object target) {
		// Same target
		if (target == getTarget()) {
			return this;
		}

		// Same fields, converter and cache
		// Different target
		StructureModifier<T> copy = new StructureModifier<>(getTargetType(), getType(), getFieldType());
		copy.init(target, fields, converter, cache);
		return copy;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <U> IStructureModifier<U> withType(Class<U> type) {
		// Preconditions
		if (type == null) {
			throw new NullPointerException("Type cannot be null");
		}

		// Same type
		if (type == getType()) {
			return (IStructureModifier<U>) this;
		}

		return (IStructureModifier<U>) cache.computeIfAbsent(type, clazz -> {
			StructureModifier<U> modifier = new StructureModifier<>(getTargetType(), type, type);
			modifier.init();
			return modifier;
		}).withTarget(getTarget());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <U> IStructureModifier<U> withConverter(IConverter<U> converter) {
		// Preconditions
		if (converter == null) {
			throw new NullPointerException("Converter cannot be null");
		}

		// Same converter
		if (converter == this.converter) {
			return (IStructureModifier<U>) this;
		}

		// Same target, fields and cache
		// Different converter
		StructureModifier<U> copy = new StructureModifier<>(getTargetType(), converter.getSpecificType(), getFieldType());
		copy.init(getTarget(), fields, converter, cache);
		return copy;
	}

	@Override
	public int size() {
		return fields.size();
	}

	@Override
	public T getSpecific(Object generic) {
		return converter == null ? getType().cast(generic) : converter.getSpecific(generic);
	}

	@Override
	public Object getGeneric(T specific) {
		return converter == null ? specific : converter.getGeneric(specific);
	}

	@Override
	protected IField getField(int index) {
		// Preconditions
		if (index < 0 || index >= size()) {
			throw new FieldAccessException("Field index out of range (fields: " + size() + ", index: " + index + ")");
		}

		IField field = fields.get(index);

		// Access
		if (!field.getField().isAccessible()) {
			try {
				field.getField().setAccessible(true);
			} catch (SecurityException exception) {
				// Nothing
			}
		}

		return field;
	}

	/**
	 * Initialize this modifier with default values.
	 * Note that a structure modifier needs to be initialized once before use.
	 */
	protected void init() {
		init(null, Collections.unmodifiableList(ReflectionHelper.fromClassFuzzy(getTargetType()).getFields(Matchers.FieldMatcher.newBuilder().withType(Matchers.ClassMatcher.newBuilder().withSuperClass(getFieldType()).build()).build(), true)), null, new ConcurrentHashMap<>());
	}

	/**
	 * Initialize this modifier with given values.
	 *
	 * @param target Modifier target.
	 * @param fields Modifier fields.
	 * @param converter Modifier converter.
	 * @param cache Subtype cache.
	 */
	protected void init(Object target, List<IField> fields, IConverter<T> converter, Map<Class<?>, IStructureModifier<?>> cache) {
		setTarget(target);
		this.fields = fields;
		this.converter = converter;
		this.cache = cache;
	}

	/**
	 * Get a structure modifier for a given class.
	 *
	 * @param clazz Class.
	 * @return the structure modifier.
	 */
	@SuppressWarnings("unchecked")
	public static IStructureModifier<Object> of(Class<?> clazz) {
		// Preconditions
		if (clazz == null) {
			throw new NullPointerException("Class cannot be null");
		}

		return (IStructureModifier<Object>) CACHE.computeIfAbsent(clazz, type -> {
			StructureModifier<?> modifier = new StructureModifier<>(type, Object.class, Object.class);
			modifier.init();
			return modifier;
		});
	}
}
