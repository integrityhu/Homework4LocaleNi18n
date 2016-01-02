package hu.integrity.web.dlg;

import hu.integrity.extp.Extension;
import hu.integrity.extp.ExtensionRegistry;
import hu.integrity.extp.ExtensionVocabulary;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

public class EditorUtils {

	public static String getPanel(Object o, String prefix) {
		if (o == null) {
			return getPanelByKey(o, "" + null, prefix);
		}

		String ed = null;

		if (o instanceof String) {
			String str = (String) o;
			ed = getPanelByKey(o, str, prefix);
		}

		if (ed != null) {
			return ed;
		}

		String ns = ExtensionVocabulary.EXT_JSF_PANEL;
		List<Extension> exts = ExtensionRegistry.getInstance().getExtensions(o,
				ns);

		Iterator<Extension> ite = exts.iterator();
		_while: while (ite.hasNext()) {
			Extension ext = ite.next();
			List<String> editorKeys = new ArrayList<>();
			Object editorKey = ext.get(o, ns);
			if (editorKey instanceof List<?>) {
				List<?> keys = (List<?>) editorKey;
				for (Object k : keys) {
					editorKeys.add("" + k);
				}
			} else {
				editorKeys.add("" + editorKey);
			}
			for (String k : editorKeys) {
				ed = getPanelByKey(o, k, prefix);
				if (ed != null) {
					break _while;
				}
			}
		}
		ed = ed != null ? ed : getPanelByKey(o, "default", prefix);
		return ed;
	}

	private static String getPanelByKey(Object o, String key, String prefix) {
		String ed = getPanelOverrideByKey(key, prefix);
		return ed != null ? ed : getPanelFromLibByKey(o, key, prefix);
	}

	private static String getPanelFromLibByKey(Object o, String key,
			String prefix) {
		if (o == null) {
			return null;
		}
		String path = "META-INF/resources/lib" + getPanelRef(key, prefix);
		ClassLoader cl = o.getClass().getClassLoader();
		if (cl == null) {
			cl = EditorUtils.class.getClassLoader();
		}
		InputStream is = cl.getResourceAsStream(path);

		String ed = is == null ? null : ("/lib" + getPanelRef(key, prefix));
		return ed;
	}

	private static String getPanelOverrideByKey(String key, String prefix) {
		ServletContext servletContext = (ServletContext) FacesContext
				.getCurrentInstance().getExternalContext().getContext();

		String editor = getPanelRef(key, prefix);
		String path = servletContext.getRealPath(editor);
		File file = new File(path);
		return file.exists() ? editor : null;
	}

	private static String getPanelRef(String n, String prefix) {
		return "/panels/" + prefix + "-" + n + ".xhtml";
	}
}
