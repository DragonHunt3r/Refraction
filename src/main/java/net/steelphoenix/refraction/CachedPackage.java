package net.steelphoenix.refraction;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A lookup cache for classes in a given package.
 *
 * @author SteelPhoenix
 */
public class CachedPackage {

	private final String name;
	private final ClassSource source;
	private final Map<String, Optional<Class<?>>> cache = new ConcurrentHashMap<>();

	public CachedPackage(String name) {
		this(name, CachedPackage.class.getClassLoader());
	}

	public CachedPackage(String name, ClassLoader loader) {
		this(name, loader == null ? null : new ClassSource(loader));
	}

	public CachedPackage(String name, ClassSource source) {
		if (source == null) {
			throw new NullPointerException("Source cannot be null");
		}

		this.name = name;
		this.source = source;
	}

	/**
	 * Retrieve a class with a given class name.
	 *
	 * @param name Class name.
	 * @return an optional of the class.
	 */
	public Optional<Class<?>> getPackageClass(String name) {
		// Preconditions
		if (name == null || name.isEmpty()) {
			throw new NullPointerException("Name cannot be null");
		}
		if (name.indexOf('.') != -1) {
			throw new IllegalArgumentException("Class is not on this package");
		}

		return cache.computeIfAbsent(name, string -> {
			String fullName = this.name == null || this.name.isEmpty() ? string : (this.name + "." + string);
			try {
				return Optional.ofNullable(source.loadClass(fullName));
			} catch (ClassNotFoundException exception) {
				return Optional.empty();
			}
		});
	}

	/**
	 * Associate a class with a given class name.
	 *
	 * @param name Class name.
	 * @param clazz Class or null to remove the associated class.
	 */
	public void setPackageClass(String name, Class<?> clazz) {
		// Preconditions
		if (name == null) {
			throw new NullPointerException("Name cannot be null");
		}

		if (clazz == null) {
			cache.remove(name);
			return;
		}
		cache.put(name, Optional.of(clazz));
	}
}
