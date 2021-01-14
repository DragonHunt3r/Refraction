package net.steelphoenix.refraction.member;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Represents an annotatable member.
 *
 * @author SteelPhoenix
 */
public interface IAnnotatableMember {

	/**
	 * Get all annotations from this member.
	 *
	 * @return the annotations.
	 */
	public Collection<Annotation> getAnnotations();

	/**
	 * Get an annotation by type from this member.
	 *
	 * @param <T> Annotation type.
	 * @param clazz Annotation type class.
	 * @return the annotation or null if the member is not annotated with it.
	 */
	public <T extends Annotation> T getAnnotation(Class<T> clazz);
}
