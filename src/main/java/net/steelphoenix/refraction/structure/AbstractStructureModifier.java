package net.steelphoenix.refraction.structure;

import java.util.Optional;

import net.steelphoenix.refraction.converter.IConverter;
import net.steelphoenix.refraction.converter.InconvertibleTypeException;
import net.steelphoenix.refraction.generator.Generators;
import net.steelphoenix.refraction.generator.IValueGenerator;
import net.steelphoenix.refraction.member.IField;

/**
 * A base structure modifier implementation.
 *
 * @param <T> Modifier type.
 *
 * @author SteelPhoenix
 */
public abstract class AbstractStructureModifier<T> implements IConverter<T>, IStructureModifier<T> {

	private final Class<?> targetType;
	private final Class<T> objectType;
	private final Class<?> fieldType;
	private Object target = null;

	protected AbstractStructureModifier(Class<?> targetType, Class<T> objectType, Class<?> fieldType) {
		if (targetType == null) {
			throw new NullPointerException("Target type cannot be null");
		}
		if (objectType == null) {
			throw new NullPointerException("Object type cannot be null");
		}
		if (fieldType == null) {
			throw new NullPointerException("Field type cannot be null");
		}

		this.targetType = targetType;
		this.objectType = objectType;
		this.fieldType = fieldType;
	}

	@Override
	public Class<?> getTargetType() {
		return targetType;
	}

	@Override
	public Class<T> getType() {
		return objectType;
	}

	@Override
	public Class<?> getFieldType() {
		return fieldType;
	}

	@Override
	public Class<T> getSpecificType() {
		return getType();
	}

	@Override
	public Object getTarget() {
		return target;
	}

	@Override
	public T read(int index) {
		// Preconditions
		if (index < 0 || index >= size()) {
			throw new FieldAccessException("Field index out of range (fields: " + size() + ", index: " + index + ")");
		}

		// Converting and getting field should always work but we catch it anyways
		try {
			return getSpecific(read0(getField(index), getTarget()));
		} catch (ClassCastException | InconvertibleTypeException exception) {
			throw new FieldAccessException("Cannot convert value to the correct type", exception);
		}
	}

	@Override
	public T readSafely(int index) {
		return index > 0 && index < size() ? read(index) : null;
	}

	@Override
	public Optional<T> readOptional(int index) {
		try {
			return Optional.ofNullable(read(index));
		} catch (FieldAccessException exception) {
			return Optional.empty();
		}
	}

	@Override
	public IStructureModifier<T> write(int index, T value) {
		// Preconditions
		if (index < 0 || index >= size()) {
			throw new FieldAccessException("Field index out of range (fields: " + size() + ", index: " + index + ")");
		}

		// Converting and getting a field should always work
		write0(getField(index), getTarget(), getGeneric(value));

		return this;
	}

	@Override
	public IStructureModifier<T> writeSafely(int index, T value) {
		if (index >= 0 && index < size()) {
			write(index, value);
		}
		return this;
	}

	@Override
	public IStructureModifier<T> write(IValueGenerator generator) {
		// Preconditions
		if (generator == null) {
			throw new NullPointerException("Generator cannot be null");
		}

		for (int i = 0; i < size(); i++) {
			IField field = getField(i);

			// The generator should generate a value of the correct type if it does its job
			write0(field, getTarget(), generator.generate(field.getField().getType()));
		}
		return this;
	}

	@Override
	public IStructureModifier<T> writeDefaults() {
		return write(Generators.getDefaultGenerator());
	}

	@Override
	public <U> IStructureModifier<U> withType(Class<?> type, IConverter<U> converter) {
		// Preconditions
		if (type == null) {
			throw new NullPointerException("Type cannot be null");
		}
		if (converter == null) {
			throw new NullPointerException("Converter cannot be null");
		}

		// Already sets target for us
		return withType(type).withConverter(converter);
	}

	/**
	 * Set the target.
	 *
	 * @param target Modifier target.
	 */
	protected void setTarget(Object target) {
		// Preconditions
		if (target != null && !targetType.isAssignableFrom(target.getClass())) {
			throw new IllegalArgumentException(target + " is not of type " + targetType);
		}

		this.target = target;
	}

	/**
	 * Get a field by index.
	 *
	 * @param index Field index.
	 * @return the field.
	 */
	protected abstract IField getField(int index);

	/**
	 * Read a field value.
	 *
	 * @param field Target field.
	 * @param target Working instance.
	 * @return the field value.
	 */
	protected static Object read0(IField field, Object target) {
		try {
			return field.get(target);
		} catch (IllegalArgumentException | IllegalStateException exception) {
			throw new FieldAccessException("Cannot read field", exception);
		}
	}

	/**
	 * Write a field value.
	 *
	 * @param field Target field.
	 * @param target Working instance.
	 * @param object Target value.
	 */
	protected static void write0(IField field, Object target, Object object) {
		try {
			field.set(target, object);
		} catch (IllegalArgumentException | IllegalStateException exception) {
			throw new FieldAccessException("Cannot write field", exception);
		}
	}
}
