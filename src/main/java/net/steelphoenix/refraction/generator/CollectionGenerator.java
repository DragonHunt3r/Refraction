package net.steelphoenix.refraction.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * A generator generating collection and map instances.
 * Initially we try to create an instance of the same time, if that fails we fall back on some Java implementations.
 * Note that this is a singleton.
 *
 * @author SteelPhoenix
 */
public class CollectionGenerator implements IValueGenerator {

	private static final IValueGenerator INSTANCE = new CollectionGenerator();

	private CollectionGenerator() {
		// Nothing
	}

	@Override
	public Object generate(Class<?> type) {
		// Preconditions
		if (type == null) {
			throw new NullPointerException("Type cannot be null");
		}

		// Only collections and maps are supported
		if (!Collection.class.isAssignableFrom(type) && !Map.class.isAssignableFrom(type)) {
			throw new UngeneratableTypeException("Type is not a collection or map");
		}

		// Try to create an instance of the exact same type
		try {
			return type.getConstructor().newInstance();
		} catch (ExceptionInInitializerError | ReflectiveOperationException | SecurityException exception) {
			// Nothing
		}

		// We only try to instantiate if the type is an interface
		// See Liskov Substitution Principle
		if (type.isInterface()) {
			if (type.equals(Collection.class)) {
				return new ArrayList<>();
			}
			else if (type.equals(List.class)) {
				return new ArrayList<>();
			}
			else if (type.equals(Map.class)) {
				return new HashMap<>();
			}
			else if (type.equals(SortedMap.class)) {
				return new TreeMap<>();
			}
			else if (type.equals(Queue.class)) {
				return new LinkedList<>();
			}
			else if (type.equals(Set.class)) {
				return new HashSet<>();
			}
			else if (type.equals(SortedSet.class)) {
				return new TreeSet<>();
			}
		}

		throw new UngeneratableTypeException("Type is not a generatable collection or map");
	}


	/**
	 * Get the instance.
	 *
	 * @return the instance.
	 */
	public static IValueGenerator getInstance() {
		return INSTANCE;
	}
}
