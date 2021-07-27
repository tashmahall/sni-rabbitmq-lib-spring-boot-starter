package br.gov.ans.snirabbitmq.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.Set;

import org.springframework.core.ConfigurableObjectInputStream;
import org.springframework.core.NestedIOException;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PatternMatchUtils;

public class SNISerializationUtils {
	/**
	 * Verify that the class is in the allowed list.
	 * @param clazz the class.
	 * @param patterns the patterns.
	 * @since 2.1
	 */
	public static void checkAllowedList(Class<?> clazz, Set<String> patterns) {
		if (ObjectUtils.isEmpty(patterns)) {
			return;
		}
		if (clazz.isArray() || clazz.isPrimitive() || clazz.equals(String.class)
				|| Number.class.isAssignableFrom(clazz)) {
			return;
		}
		String className = clazz.getName();
		for (String pattern : patterns) {
			if (PatternMatchUtils.simpleMatch(pattern, className)) {
				return;
			}
		}
		throw new SecurityException("Attempt to deserialize unauthorized " + clazz);
	}	
	/**
	 * Deserialize the stream.
	 * @param inputStream the stream.
	 * @param allowedListPatterns allowed classes.
	 * @param classLoader the class loader.
	 * @return the result.
	 * @throws IOException IO Exception.
	 * @since 2.1
	 */
	public static Object deserialize(InputStream inputStream, final Set<String> allowedListPatterns, ClassLoader classLoader)
			throws IOException {

		try (
			ObjectInputStream objectInputStream = new ConfigurableObjectInputStream(inputStream, classLoader) {

				@Override
				protected Class<?> resolveClass(ObjectStreamClass classDesc)
						throws IOException, ClassNotFoundException {
					Class<?> clazz = super.resolveClass(classDesc);
					checkAllowedList(clazz, allowedListPatterns);
					return clazz;
				}

			}) {

			return objectInputStream.readObject();
		}
		catch (ClassNotFoundException ex) {
			throw new NestedIOException("Failed to deserialize object type", ex);
		}
	}
}
