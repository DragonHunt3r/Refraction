package net.steelphoenix.refraction.member;

import java.util.Collection;

/**
 * A class wrapper.
 *
 * @author SteelPhoenix
 */
public interface IType extends IMember {

	/**
	 * Get all constructors declared in this class.
	 *
	 * @return all constructors.
	 */
	public Collection<IConstructor> getConstructors();

	/**
	 * Get all fields declared in this class.
	 *
	 * @return all fields.
	 */
	public Collection<IField> getFields();

	/**
	 * Get all methods declared in this class.
	 *
	 * @return all methods.
	 */
	public Collection<IMethod> getMethods();

	/**
	 * Get all classes declared in this class.
	 *
	 * @return all classes.
	 */
	public Collection<IType> getTypes();

	/**
	 * Get if this class is an array.
	 *
	 * @return if this class is an array.
	 */
	public boolean isArray();

	/**
	 * Get if this class is an interface.
	 *
	 * @return if this class is an interface.
	 */
	public boolean isEnum();

	/**
	 * Get if this type is in the given class's class hierarchy.
	 * This also means isSuperClassOf(getType()) is true.
	 *
	 * @param clazz Class.
	 * @return if this is a superclass of the given class.
	 */
	public boolean isSuperClassOf(Class<?> clazz);

	/**
	 * Get if the given class is in this type's class hierarchy.
	 * This also means isSubClassOf(getType()) is true.
	 *
	 * @param clazz Class.
	 * @return if this is a subclass of the given class.
	 */
	public boolean isSubClassOf(Class<?> clazz);

	/**
	 * Get the underlying class.
	 *
	 * @return the class.
	 */
	public Class<?> getType();
}
