package hu.integrity.extp;

import hu.integrity.web.i18n.LocaleBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.text.ZoneView;

public class ClassNamePanelResolver implements Extension {

	@Override
	public boolean match(Object o, String ns) {
		return null != get(o, ns);
	}

	@Override
	public Object get(Object o, String ns) {
		if (ExtensionVocabulary.EXT_JSF_PANEL.equals(ns)) {
			if (o == null) {
				return null;
			}
			List<String> classes = new ArrayList<>();
			List<Class<?>> cc = getClasses(o);
			for (Class<?> c : cc) {
				classes.add(c.getSimpleName());
			}
			return classes;
		}
		return null;
	}

	private List<Class<?>> getClasses(Object o) {
		List<Class<?>> cc = new ArrayList<>();
		Class<?> c = o.getClass();
		while (c != null) {
			cc.add(c);
			c = c.getSuperclass();
		}
		return cc;
	}

	@Override
	public int getRank(Object o, String ns) {
		if (!ExtensionVocabulary.EXT_JSF_PANEL.equals(ns)) {
			return Extension.RANK_TYPE;
		}
		return Extension.RANK_NONE;
	}
}
