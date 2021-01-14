package net.steelphoenix.refraction;

/**
 * A class source wrapping a class loader.
 * This class allows for class name remapping when overriding {@link #getClassName(String)}.
 *
 * @author SteelPhoenix
 */
public class ClassSource {

	private final ClassLoader loader;

	public ClassSource() {
		this(ClassSource.class.getClassLoader());
	}

	public ClassSource(ClassLoader loader) {
		if (loader == null) {
			throw new NullPointerException("Loader cannot be null");
		}

		this.loader = loader;
	}

	/**
	 * Load a class by name.
	 *
	 * @param name Class name.
	 * @return the loaded class.
	 * @throws ClassNotFoundException If the class was not found.
	 */
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		if (name == null) {
			throw new NullPointerException("Name cannot be null");
		}

		return loader.loadClass(getClassName(name));
	}

	/**
	 * Get a class name from a class name.
	 * This method allows for remapping classes.
	 *
	 * @param name Class name.
	 * @return the possibly remapped class name.
	 */
	public String getClassName(String name) {
		return name;
	}
}
