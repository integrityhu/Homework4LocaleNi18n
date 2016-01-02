package hu.integrity.web.i18n;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@SessionScoped
@Named("loc")
public class LocaleBean implements Serializable {

	public static final String PREF_LOCALE = "pref.locale";
	public static final String PREF_TIMEZONE = "pref.timezone";

	private TimeZone timeZone;

	public TimeZone getTimeZone() {
		if (timeZone == null) {
			timeZone = TimeZone.getDefault();
		}
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public static TimeZone asTimeZone(String code) {
		List<TimeZone> tzs = getTimeZones();
		for (TimeZone tz : tzs) {
			if (asString(tz).equals(code)) {
				return tz;
			}
		}
		return null;
	}

	public static String asString(TimeZone tz) {
		return tz.getDisplayName(Locale.ENGLISH);
	}

	public String getTimeZoneName() {
		return getTimeZone().getDisplayName(getLocale());
	}

	public String getTimeZoneNameByCode(String code) {
		TimeZone tz = asTimeZone(code);
		return tz.getDisplayName(getLocale());
	}

	public List<String> getTimeZoneNames() {
		String[] ids = TimeZone.getAvailableIDs();
		List<String> timeZones = new ArrayList<>();
		for (String id : ids) {
			TimeZone tz = TimeZone.getTimeZone(id);
			timeZones.add(tz.getDisplayName(getLocale()));
		}
		return timeZones;
	}

	public static List<TimeZone> getTimeZones() {
		String[] ids = TimeZone.getAvailableIDs();
		List<TimeZone> timeZones = new ArrayList<>();
		Arrays.sort(ids);
		for (int i = 0; i < ids.length; i++) {
			TimeZone tz = TimeZone.getTimeZone(ids[i]);
			timeZones.add(tz);
		}
		return timeZones;
	}

	public List<String> getTimeZoneCodes() {
		List<TimeZone> tzs = getTimeZones();
		List<String> codes = new ArrayList<>();
		for (TimeZone tz : tzs) {
			codes.add(asString(tz));
		}
		return codes;
	}

	private Locale locale;

	public Locale getLocale() {
		if (locale == null) {
			locale = FacesContext.getCurrentInstance().getApplication()
					.getDefaultLocale();
		}
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
		FacesContext.getCurrentInstance().getViewRoot().setLocale(getLocale());
	}

	public static Locale asLocale(String langTag) {
		List<Locale> locales = getLocales();
		for (Locale locale : locales) {
			if (asString(locale).equals(langTag)) {
				return locale;
			}
		}
		return null;
	}

	public static String asString(Locale locale) {
		return locale.toLanguageTag();
	}

	public String getLanguage() {
		return getLanguage(getLocale());
	}

	public String getLanguageByCode(String code) {
		Locale loc = getLocale(code);
		return getLanguage(loc);
	}

	public Locale getLocale(String code) {
		List<Locale> locales = getLocales();
		for (Locale locale : locales) {
			if (asString(locale).equals(code)) {
				return locale;
			}
		}
		return null;
	}

	public List<String> getLocaleCodes() {
		List<String> codes = new ArrayList<>();
		List<Locale> locales = getLocales();
		for (Locale locale : locales) {
			codes.add(asString(locale));
		}
		return codes;
	}

	public static List<Locale> getLocales() {
		Application app = FacesContext.getCurrentInstance().getApplication();
		List<Locale> locales = new ArrayList<>();
		locales.add(app.getDefaultLocale());
		Iterator<Locale> ite = app.getSupportedLocales();
		while (ite.hasNext()) {
			Locale locale = ite.next();
			locales.add(locale);
		}
		return locales;
	}

	public List<String> getLanguages() {
		List<String> languages = new ArrayList<>();
		List<Locale> locales = getLocales();
		for (Locale locale : locales) {
			languages.add(getLanguage(locale));
		}
		return languages;
	}

	private String getLanguage(Locale locale) {
		String lang = locale.getDisplayLanguage(locale);
		if (lang == null) {
			lang = locale.toString();
		}
		return lang;
	}

	public void setLanguage(String language) {
		List<Locale> locales = getLocales();
		for (Locale locale : locales) {
			if (language.equals(getLanguage(locale))) {
				this.setLocale(locale);
			}
		}
	}

	private static final long serialVersionUID = 1L;
}
