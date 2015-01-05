package cl.facele.docele.aguasanta;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Resource {
	private static final String BUNDLE_NAME = "AGUASANTA";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	/**
	 * Constructor vaci­o para las propiedades
	 */
	private Resource() {
	}

	/**
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key).trim();
		} catch (MissingResourceException e) {
			// return '!' + key + '!';
			return null;
		}
	}

	public static boolean getBoolean(String key) {
		String s = getString(key);
		if (s != null)
			s = s.toLowerCase();
		return ("yes".equals(s) || "true".equals(s) || "1".equals(s));
	}

	public static int getInt(String key) {
		String s = getString(key);
		if (s != null) {
			s = s.toLowerCase();
			return Integer.parseInt(s);
		}
		return 0;
	}

	public static Integer getInteger(String key) {
		String s = getString(key);
		if (s != null) {
			s = s.toLowerCase();
			return new Integer(s);
		}
		return null;
	}

}
