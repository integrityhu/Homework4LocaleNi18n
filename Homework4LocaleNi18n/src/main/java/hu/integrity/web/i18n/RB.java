/**
 * 
 */
package hu.integrity.web.i18n;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

/**
 * @author pzoli
 * www.techques.com/question/1-9080474/messages.properties-taken-from-db
 * 
 */
public class RB extends ResourceBundle {

    public RB() {
        String baseName = getClass().getName();
        List<Locale> locales = LocaleBean.getLocales();
        Control control = new RBControl();
        for (Locale locale : locales) {
            ResourceBundle p = ResourceBundle.getBundle(baseName, locale,
                    control);
            setParent(p);
        }
    }

    @Override
    protected Object handleGetObject(String key) {
        Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        return I18N_DB.getString(key, locale); 
    }

    @Override
    public Enumeration<String> getKeys() {
        return null;
    }

    protected class RBControl extends Control {

        @Override
        public ResourceBundle newBundle(String baseName, Locale locale,
                String format, ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException,
                IOException {
            return new DBResources(locale);
        }

        protected class DBResources extends ListResourceBundle {

            private Locale locale;

            public DBResources(Locale locale) {
                this.locale = locale;
            }

            @Override
            protected Object[][] getContents() {
                // TODO load key/value pairs from db
                Object[][] aa = { { "label.preferences", "pref - " + locale } };
                return aa;
            }
        }
    }

}
