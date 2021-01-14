package net.steelphoenix.refraction;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.function.Function;

/**
 * A util class.
 *
 * @author SteelPhoenix
 */
public class Util {

	private Util() {
		// Nothing
	}

	/**
	 * A tuple from an entry because Java does not include those.
	 * This entry is immutable.
	 *
	 * @param <K> Key type.
	 * @param <V> Value type.
	 * @param k Key.
	 * @param v Value.
	 * @return the tuple.
	 */
	public static <K, V> Entry<K, V> newTuple(K k, V v) {
		return new SimpleImmutableEntry<>(k, v);
	}

	/**
	 * Get if two methods have the same handle.
	 *
	 * @param method1 First method.
	 * @param method2 Second method.
	 * @return if the methods have the same handle.
	 */
	public static boolean isSameHandle(Method method1, Method method2) {
		// Preconditions
		if (method1 == null) {
			throw new NullPointerException("First method cannot be null");
		}
		if (method2 == null) {
			throw new NullPointerException("Second method cannot be null");
		}

		return method1.getName().equals(method2.getName()) && Arrays.equals(method1.getParameterTypes(), method2.getParameterTypes());
	}

	/**
	 * Get a string version of a class.
	 *
	 * @param clazz Class.
	 * @return a string version.
	 */
	public static String classToString(Class<?> clazz) {
		return clazz == null ? "<any>" : clazz.getTypeName();
	}

	/**
	 * Get a string version of a constructor handle.
	 *
	 * @param constructor Constructor.
	 * @return a string version.
	 */
	public static String constructorToString(Constructor<?> constructor) {
		// Preconditions
		if (constructor == null) {
			throw new NullPointerException("Constructor cannot be null");
		}

		return constructorToString(constructor.getDeclaringClass(), constructor.getParameterTypes());
	}

	/**
	 * Get a string version of a constructor handle.
	 *
	 * @param clazz Declaring class.
	 * @param params Constructor parameter types.
	 * @return a string version.
	 */
	public static String constructorToString(Class<?> clazz, Class<?>... params) {
		// We abuse the the API here by knowing methods are formatted the same way
		return methodToString(clazz, "<init>", params);
	}

	/**
	 * Get a string version of a field handle.
	 *
	 * @param field Field.
	 * @return a string version.
	 */
	public static String fieldToString(Field field) {
		// Preconditions
		if (field == null) {
			throw new NullPointerException("Field cannot be null");
		}

		return fieldToString(field.getDeclaringClass(), field.getName(), field.getType());
	}

	/**
	 * Get a string version of a field handle.
	 *
	 * @param clazz Declaring class.
	 * @param name Field name.
	 * @param type Field type.
	 * @return a string version.
	 */
	public static String fieldToString(Class<?> clazz, String name, Class<?> type) {
		return new StringBuilder()
				.append(classToString(clazz))
				.append('#')
				.append(name == null ? "<any>" : name)
				.append(" (")
				.append(classToString(type))
				.append(')')
				.toString();
	}

	/**
	 * Get a string version of a method handle.
	 *
	 * @param method Method.
	 * @return a string version.
	 */
	public static String methodToString(Method method) {
		// Preconditions
		if (method == null) {
			throw new NullPointerException("Method cannot be null");
		}

		return methodToString(method.getDeclaringClass(), method.getName(), method.getParameterTypes());
	}

	/**
	 * Get a string version of a method handle.
	 *
	 * @param clazz Declaring class.
	 * @param name Method name.
	 * @param params Method parameter types.
	 * @return a string version.
	 */
	public static String methodToString(Class<?> clazz, String name, Class<?>... params) {
		StringBuilder builder = new StringBuilder()
				.append(classToString(clazz))
				.append('#')
				.append(name == null ? "<any>" : name)
				.append('(');

		if (params == null) {
			builder.append("<any>...");
		}
		else {
			for (int i = 0; i < params.length; i++) {
				if (i != 0) {
					builder.append(", ");
				}

				builder.append(classToString(params[i]));
			}
		}

		return builder.append(')').toString();
	}

	/**
	 * Print an array to a writer.
	 *
	 * @param <T> Array type.
	 * @param writer Writer.
	 * @param array Array.
	 * @param toString Element to string function.
	 */
	public static <T> void printArray(PrintWriter writer, T[] array, Function<T, String> toString) {
		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				writer.print(", ");
			}
			writer.print(toString.apply(array[i]));
		}
	}
}
