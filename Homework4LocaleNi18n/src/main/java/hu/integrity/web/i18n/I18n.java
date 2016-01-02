package hu.integrity.web.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {
	private static final String MARK_UNKNOWN = "??";

	private static final String I18N = "i18n";

	private static ResourceBundle CURRENTBUNDLE;
	private static Locale CURRENTLOCALE;

	public static String getString(String key, Locale locale) {
		if (!locale.equals(I18n.CURRENTLOCALE)) {
			try {
				I18n.CURRENTLOCALE = locale;
				CURRENTBUNDLE = ResourceBundle.getBundle(I18N, locale);
				//http://stackoverflow.com/questions/4659929/how-to-use-utf-8-in-resource-properties-with-resourcebundle
				//CURRENTBUNDLE = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
			} catch (Exception e) {
				// skip
			}
		}
		String text = null;
		try {
			if (CURRENTBUNDLE != null) {
				text = CURRENTBUNDLE.getString(key);
			}
		} catch (Exception e) {
			// skip
		}
		return text == null ? MARK_UNKNOWN + key + MARK_UNKNOWN : text;
	}
}