package net.steelphoenix.refraction.converter;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import net.steelphoenix.refraction.generator.Generators;

/**
 * A utility class for converters.
 *
 * @author SteelPhoenix
 */
public class Converters {

	private Converters() {
		// Nothing
	}

	/**
	 * Get a converter that rethrows all runtime exceptions as {@link InconvertibleTypeException}.
	 *
	 * @param <T> Converter type.
	 * @param converter Source converter.
	 * @return the changed converter.
	 */
	public static <T> IConverter<T> convertSafe(IConverter<T> converter) {
		// Preconditions
		if (converter == null) {
			throw new NullPointerException("Converter cannot be null");
		}

		return new IConverter<T>() {

			@Override
			public T getSpecific(Object generic) {
				try {
					return converter.getSpecific(generic);
				} catch (InconvertibleTypeException exception) {
					throw exception;
				} catch (RuntimeException exception) {
					throw new InconvertibleTypeException("Cannot convert to specific type", exception);
				}
			}

			@Override
			public Object getGeneric(T specific) {
				try {
					return converter.getGeneric(specific);
				} catch (InconvertibleTypeException exception) {
					throw exception;
				} catch (RuntimeException exception) {
					throw new InconvertibleTypeException("Cannot convert to generic type", exception);
				}
			}

			@Override
			public Class<T> getSpecificType() {
				return converter.getSpecificType();
			}
		};
	}

	/**
	 * Get a converter that forwards null values.
	 *
	 * @param <T> Converter type.
	 * @param converter Source converter.
	 * @return the changed converter.
	 */
	public static <T> IConverter<T> convertNull(IConverter<T> converter) {
		// Preconditions
		if (converter == null) {
			throw new NullPointerException("Converter cannot be null");
		}

		return new IConverter<T>() {

			@Override
			public T getSpecific(Object generic) {
				return generic == null ? null : converter.getSpecific(generic);
			}

			@Override
			public Object getGeneric(T specific) {
				return specific == null ? null : converter.getGeneric(specific);
			}

			@Override
			public Class<T> getSpecificType() {
				return converter.getSpecificType();
			}
		};
	}

	/**
	 * Get a converter that converts collection elements.
	 *
	 * @param <T> Collection type.
	 * @param <U> Converter type.
	 * @param clazz Collection type class.
	 * @param converter Source converter.
	 * @return the changed converter.
	 */
	public static <T extends Collection<U>, U> IConverter<T> convertCollection(Class<T> clazz, IConverter<U> converter) {
		// Preconditions
		if (clazz == null) {
			throw new NullPointerException("Collection type cannot be null");
		}
		if (converter == null) {
			throw new NullPointerException("Converter cannot be null");
		}

		return convertSafe(new IConverter<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T getSpecific(Object generic) {
				Collection<?> collection = (Collection<?>) generic;

				// Result collection of the correct type
				Collection<U> result =  (Collection<U>) Generators.getCollectionGenerator().generate(clazz);

				// Convert elements
				for (Object object : collection) {
					result.add(converter.getSpecific(object));
				}

				return clazz.cast(result);
			}

			@SuppressWarnings("unchecked")
			@Override
			public Object getGeneric(T specific) {
				Collection<Object> result = (Collection<Object>) Generators.getCollectionGenerator().generate(specific.getClass());

				// Convert elements
				for (U u : specific) {
					result.add(converter.getGeneric(u));
				}

				return result;
			}

			@Override
			public Class<T> getSpecificType() {
				return clazz;
			}
		});
	}

	/**
	 * Get a converter that converts map entries.
	 *
	 * @param <T> Map type.
	 * @param <K> Key type.
	 * @param <V> Value type.
	 * @param clazz Map type class.
	 * @param keyConverter Source key converter.
	 * @param valueConverter Source value converter.
	 * @return the changed converter.
	 */
	public static <T extends Map<K, V>, K, V> IConverter<T> convertMap(Class<T> clazz, IConverter<K> keyConverter, IConverter<V> valueConverter) {
		// Preconditions
		if (clazz == null) {
			throw new NullPointerException("Collection type cannot be null");
		}
		if (keyConverter == null) {
			throw new NullPointerException("Key converter cannot be null");
		}
		if (valueConverter == null) {
			throw new NullPointerException("Value converter cannot be null");
		}

		return convertSafe(new IConverter<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T getSpecific(Object generic) {
				Map<?, ?> map = (Map<?, ?>) generic;

				// Result map of the correct type
				Map<K, V> result =  (Map<K, V>) Generators.getCollectionGenerator().generate(clazz);

				// Convert elements
				for (Entry<?, ?> entry : map.entrySet()) {
					result.put(keyConverter.getSpecific(entry.getKey()), valueConverter.getSpecific(entry.getValue()));
				}

				return clazz.cast(result);
			}

			@SuppressWarnings("unchecked")
			@Override
			public Object getGeneric(T specific) {
				Map<Object, Object> result = (Map<Object, Object>) Generators.getCollectionGenerator().generate(specific.getClass());

				// Convert elements
				for (Entry<K, V> entry : specific.entrySet()) {
					result.put(keyConverter.getGeneric(entry.getKey()), valueConverter.getGeneric(entry.getValue()));
				}

				return result;
			}

			@Override
			public Class<T> getSpecificType() {
				return clazz;
			}
		});
	}

	/**
	 * Get a converter that converts array elements.
	 *
	 * @param <T> Converter type.
	 * @param converter Source converter.
	 * @return the changed converter.
	 */
	public static <T> IConverter<T[]> convertArray(IConverter<T> converter) {
		// Preconditions
		if (converter == null) {
			throw new NullPointerException("Converter cannot be null");
		}

		return convertSafe(new IConverter<T[]>() {

			@SuppressWarnings("unchecked")
			@Override
			public T[] getSpecific(Object generic) {
				Object[] array = (Object[]) generic;
				T[] result = (T[]) Array.newInstance(converter.getSpecificType(), array.length);

				// Convert elements
				for (int i = 0; i < array.length; i++) {
					result[i] = converter.getSpecific(array[i]);
				}

				return result;
			}

			@Override
			public Object getGeneric(T[] specific) {
				Object[] generic = new Object[specific.length];

				// Convert elements
				for (int i = 0; i < specific.length; i++) {
					generic[i] = converter.getGeneric(specific[i]);
				}

				return generic;
			}

			@SuppressWarnings("unchecked")
			@Override
			public Class<T[]> getSpecificType() {
				// This is not clean, Class#arrayType() was only introduced in Java 12
				return (Class<T[]>) Array.newInstance(converter.getSpecificType(), 0).getClass();
			}
		});
	}

	/**
	 * Get a converter based on functions.
	 *
	 * @param <T> Converter type.
	 * @param specific Specific conversion function.
	 * @param generic Generic conversion function.
	 * @param type Specific type class.
	 * @return the converter.
	 */
	public static <T> IConverter<T> of(Function<Object, T> specific, Function<T, Object> generic, Class<T> type) {
		// Preconditions
		if (specific == null) {
			throw new NullPointerException("Specific function cannot be null");
		}
		if (generic == null) {
			throw new NullPointerException("Generic function cannot be null");
		}
		if (type == null) {
			throw new NullPointerException("Type cannot be null");
		}

		return convertSafe(new IConverter<T>() {

			@Override
			public T getSpecific(Object generic) {
				return specific.apply(generic);
			}

			@Override
			public Object getGeneric(T specific) {
				return generic.apply(specific);
			}

			@Override
			public Class<T> getSpecificType() {
				return type;
			}
		});
	}
}
