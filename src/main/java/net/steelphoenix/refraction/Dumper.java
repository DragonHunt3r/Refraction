package net.steelphoenix.refraction;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Member data dumper.
 *
 * @author SteelPhoenix
 *
 * @deprecated Not strictly part of the API, helpful for analysis.
 */
@Deprecated
public class Dumper {

	/**
	 * Dump class info.
	 *
	 * @param writer Writer.
	 * @param clazz Class.
	 */
	public static void dump(PrintWriter writer, Class<?> clazz) {
		// Preconditions
		if (writer == null) {
			throw new NullPointerException("Writer cannot be null");
		}
		if (clazz == null) {
			throw new NullPointerException("Class cannot be null");
		}

		// Modifier
		writer.print(clazz.getModifiers());
		writer.print(' ');

		// Class type
		if (clazz.isAnnotation()) {
			writer.print("@interface");
		}
		else if (clazz.isEnum()) {
			writer.print("enum");
		}
		else if (clazz.isInterface()) {
			writer.print("interface");
		}
		else {
			writer.print("class");
		}
		writer.print(' ');

		// Name
		writer.print(clazz.getTypeName());
		writer.print(' ');

		// Superclass
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null) {
			writer.print("extends ");
			writer.print(superClass.getTypeName());
			writer.print(' ');
		}

		// Interfaces
		Class<?>[] ifaces = clazz.getInterfaces();
		if (ifaces.length != 0) {
			writer.print("implements ");
			Util.printArray(writer, ifaces, Class::getTypeName);
		}

		writer.print('{');
		writer.println();

		// Fields
		for (Field field : clazz.getDeclaredFields()) {
			dump(writer, field);
		}

		// Constructors
		for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
			dump(writer, constructor);
		}

		// Methods
		for (Method method : clazz.getDeclaredMethods()) {
			dump(writer, method);
		}

		// Nested classes
		for (Class<?> nested : clazz.getDeclaredClasses()) {
			dump(writer, nested);
		}


		writer.print('}');

		// Synthetic
		if (clazz.isSynthetic()) {
			writer.print(" // synthetic");
		}

		writer.println();
	}

	/**
	 * Dump constructor info.
	 *
	 * @param writer Writer.
	 * @param constructor Constructor.
	 */
	public static void dump(PrintWriter writer, Constructor<?> constructor) {
		// Preconditions
		if (writer == null) {
			throw new NullPointerException("Writer cannot be null");
		}
		if (constructor == null) {
			throw new NullPointerException("Constructor cannot be null");
		}

		// Modifier
		writer.print(constructor.getModifiers());
		writer.print(' ');

		// Name
		// Class name is already printed so we just use "<init>" to make it more clear
		writer.print("<init>(");

		// Params
		Class<?>[] params = constructor.getParameterTypes();
		Util.printArray(writer, params, Class::getTypeName);

		writer.print(')');

		// Exceptions
		Class<?>[] throwables = constructor.getExceptionTypes();
		if (throwables.length != 0) {
			writer.print(" throws ");
			Util.printArray(writer, throwables, Class::getTypeName);
		}

		writer.print(';');

		// Synthetic
		if (constructor.isSynthetic()) {
			writer.print(" // synthetic");
		}

		writer.println();
	}

	/**
	 * Dump field info.
	 *
	 * @param writer Writer.
	 * @param field Field.
	 */
	public static void dump(PrintWriter writer, Field field) {
		// Preconditions
		if (writer == null) {
			throw new NullPointerException("Writer cannot be null");
		}
		if (field == null) {
			throw new NullPointerException("Field cannot be null");
		}

		// Modifier
		writer.print(field.getModifiers());
		writer.print(' ');

		// Type
		writer.print(field.getType().getTypeName());
		writer.print(' ');

		// Name
		writer.print(field.getName());
		writer.print(';');

		// Synthetic
		if (field.isSynthetic()) {
			writer.print(" // synthetic");
		}

		writer.println();
	}

	/**
	 * Dump method info.
	 *
	 * @param writer Writer.
	 * @param method Method.
	 */
	public static void dump(PrintWriter writer, Method method) {
		// Preconditions
		if (writer == null) {
			throw new NullPointerException("Writer cannot be null");
		}
		if (method == null) {
			throw new NullPointerException("Method cannot be null");
		}

		// Modifier
		writer.print(method.getModifiers());
		writer.print(' ');

		// Return type
		writer.print(method.getReturnType().getTypeName());
		writer.print(' ');

		// Name
		writer.print(method.getName());
		writer.print('(');

		// Params
		Class<?>[] params = method.getParameterTypes();
		Util.printArray(writer, params, Class::getTypeName);

		writer.print(')');

		// Exceptions
		Class<?>[] throwables = method.getExceptionTypes();
		if (throwables.length != 0) {
			writer.print(" throws ");
			Util.printArray(writer, throwables, Class::getTypeName);
		}

		writer.print(';');

		// Synthetic
		if (method.isSynthetic()) {
			writer.print(" // synthetic");
		}

		writer.println();
	}
}
