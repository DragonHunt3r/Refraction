package net.steelphoenix.refraction.member;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import net.steelphoenix.refraction.Util;
import net.steelphoenix.refraction.primitives.IPrimitive;
import net.steelphoenix.refraction.primitives.Primitives;

/**
 * A default class wrapper implementation.
 *
 * @author SteelPhoenix
 */
public class SimpleType implements IType {

	private final Class<?> type;

	public SimpleType(Class<?> type) {
		if (type == null) {
			throw new NullPointerException("Type cannot be null");
		}

		this.type = type;
	}

	@Override
	public Collection<IConstructor> getConstructors() {
		return Arrays.stream(type.getDeclaredConstructors()).map(Members::wrap).collect(Collectors.toSet());
	}

	@Override
	public Collection<IField> getFields() {
		return Arrays.stream(type.getDeclaredFields()).map(Members::wrap).collect(Collectors.toSet());
	}

	@Override
	public Collection<IMethod> getMethods() {
		return Arrays.stream(type.getDeclaredMethods()).map(Members::wrap).collect(Collectors.toSet());
	}

	@Override
	public Collection<IType> getTypes() {
		return Arrays.stream(type.getDeclaredClasses()).map(Members::wrap).collect(Collectors.toSet());
	}

	@Override
	public boolean isArray() {
		return type.isArray();
	}

	@Override
	public boolean isEnum() {
		return type.isEnum();
	}

	@Override
	public boolean isSuperClassOf(Class<?> clazz) {
		// Preconditions
		if (clazz == null) {
			throw new NullPointerException("Class cannot be null");
		}

		// Auto boxing is a thing and for primitives isAssignableFrom(...) is false so we use their boxed types
		IPrimitive primitive = Primitives.getByType(clazz);
		return type.isAssignableFrom(primitive == null ? clazz : primitive.getBoxedType());
	}

	@Override
	public boolean isSubClassOf(Class<?> clazz) {
		// Preconditions
		if (clazz == null) {
			throw new NullPointerException("Class cannot be null");
		}

		// Note that we do not do anything special for primitives, as they cannot be null
		return clazz.isAssignableFrom(type);
	}

	@Override
	public Class<?> getType() {
		return type;
	}

	@Override
	public int getModifiers() {
		return type.getModifiers();
	}

	@Override
	public Collection<Annotation> getAnnotations() {
		return Arrays.stream(type.getDeclaredAnnotations()).collect(Collectors.toSet());
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> clazz) {
		// Preconditions
		if (clazz == null) {
			throw new NullPointerException("Type cannot be null");
		}

		return type.getDeclaredAnnotation(clazz);
	}

	@Override
	public boolean isSynthetic() {
		return type.isSynthetic();
	}

	@Override
	public Class<?> getDeclarer() {
		return type.getDeclaringClass();
	}

	@Override
	public String getName() {
		return type.getTypeName();
	}

	@Override
	public int hashCode() {
		return type.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof IType)) {
			return false;
		}

		return type.equals(((IType) object).getType());
	}

	@Override
	public String toString() {
		return "Type[type=" + Util.classToString(type) + "]";
	}
}
