package net.steelphoenix.refraction.member;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import net.steelphoenix.refraction.Util;

/**
 * A default field wrapper implementation.
 *
 * @author SteelPhoenix
 */
public class SimpleField implements IField {

	private final Field field;

	public SimpleField(Field field) {
		if (field == null) {
			throw new NullPointerException("Field cannot be null");
		}

		this.field = field;
	}

	@Override
	public Object get(Object instance) {
		// Preconditions
		if (!isStatic() && instance == null) {
			// No NPE because technically null instances are supported if the field is static
			throw new IllegalArgumentException("Instance cannot be null for non-static fields");
		}

		try {
			return field.get(instance);
		} catch (IllegalAccessException exception) {
			// Not invokable
			throw new IllegalStateException("Cannot get field", exception);
		}
	}

	@Override
	public void set(Object instance, Object value) {
		// Preconditions
		if (!isStatic() && instance == null) {
			// No NPE because technically null instances are supported if the field is static
			throw new IllegalArgumentException("Instance cannot be null for non-static fields");
		}

		try {
			field.set(instance, value);
		} catch (IllegalAccessException exception) {
			// Not invokable
			throw new IllegalStateException("Cannot set field", exception);
		}
	}

	@Override
	public Field getField() {
		return field;
	}

	@Override
	public IField asReadOnly() {
		return new SimpleReadOnlyField(getField());
	}

	@Override
	public int hashCode() {
		return field.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof IField)) {
			return false;
		}

		return field.equals(((IField) object).getField());
	}

	@Override
	public int getModifiers() {
		return field.getModifiers();
	}

	@Override
	public Collection<Annotation> getAnnotations() {
		return Arrays.stream(field.getDeclaredAnnotations()).collect(Collectors.toSet());
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> clazz) {
		// Preconditions
		if (clazz == null) {
			throw new NullPointerException("Type cannot be null");
		}

		return field.getDeclaredAnnotation(clazz);
	}

	@Override
	public boolean isSynthetic() {
		return field.isSynthetic();
	}

	@Override
	public Class<?> getDeclarer() {
		return field.getDeclaringClass();
	}

	@Override
	public String getName() {
		return field.getName();
	}

	@Override
	public String toString() {
		return "Field[field=" + Util.fieldToString(field) + "]";
	}

	/**
	 * A read only implementation where trying to set a value throws an exception.
	 *
	 * @author SteelPhoenix
	 */
	private static class SimpleReadOnlyField extends SimpleField {
		public SimpleReadOnlyField(Field field) {
			super(field);
		}

		@Override
		public void set(Object instance, Object value) {
			// We cannot change values because we are read only
			throw new UnsupportedOperationException("Field accessor is read only");
		}

		@Override
		public IField asReadOnly() {
			// We are already read only
			return this;
		}
	}
}