package hu.integrity.web.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Map.Entry;
import java.util.Set;

import hu.integrity.web.dlg.Dialog;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ConversationScoped;

@Named("prefDlg")
@ConversationScoped
public class PreferencesDialog extends Dialog<Map<String, Object>> {

	@PostConstruct
	private void pc() {
		header = "label.preferences";
		editable = true;
		crud = true;
		src = "/preferences.xhtml";
		update = "@all";
		deletable = false;
	}

	@Inject
	private LocaleBean loc;

	@Override
	public Map<String, Object> getBean() {
		if (bean == null) {
			bean = new HashMap<>();
			init(bean);
		}
		return super.getBean();
	}

	@Override
	public void save() {
		Set<Entry<String, Object>> es = bean.entrySet();
		for (Entry<String, Object> entry : es) {
			save(entry.getKey(), entry.getValue());
		}
		super.save();
	}

	protected void save(String key, Object value) {
		if (LocaleBean.PREF_LOCALE.equals(key)) {
			Locale locale = null;
			if (value instanceof Locale) {
				locale = (Locale) value;
			} else {
				locale = LocaleBean.asLocale("" + value);
			}
			if (locale != null && !loc.getLocale().equals(locale)) {
				loc.setLocale(locale);
			}
		}
		if (LocaleBean.PREF_TIMEZONE.equals(key)) {
			TimeZone tz = null;
			if (value instanceof TimeZone) {
				tz = (TimeZone) value;
			} else {
				tz = LocaleBean.asTimeZone("" + value);
			}
			if (tz != null && !loc.getTimeZone().equals(tz)) {
				loc.setTimeZone(tz);
			}
		}
	}

	protected void init(Map<String, Object> bean) {
		bean.put(LocaleBean.PREF_LOCALE, loc.getLocale());
		bean.put(LocaleBean.PREF_TIMEZONE, loc.getTimeZone());
	}

	private static final long serialVersionUID = 1L;
}
