package hu.integrity.web.dlg;

import hu.integrity.extp.Extension;
import hu.integrity.extp.ExtensionRegistry;
import hu.integrity.web.i18n.I18n;
import hu.integrity.web.spa.Window;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

public class Dialog<T> implements Serializable {

	public String getLocalized(String key) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		Locale locale = ctx == null ? Locale.getDefault() : ctx.getViewRoot()
				.getLocale();
		String text = I18n.getString(key, locale);
		return text;
	}

	public boolean isUpdated(String field) {
		Boolean u = uMap.get(field);
		return u == null ? false : u;
	}

	private Map<String, Boolean> uMap = new HashMap<>();

	public void setUpdated(String field, boolean updated) {
		if (updated) {
			this.updated = updated;
		}
		uMap.put(field, updated);
	}

	public String calcID(String localID) {
		String id = localID;
		if (marker != null) {
			id = ":" + marker.getNamingContainer().getId() + ":" + id;
		}
		return id;
	}

	public UIComponent getMarker() {
		return marker;
	}

	public void setMarker(UIComponent marker) {
		this.marker = marker;
	}

	private UIComponent marker;

	@SuppressWarnings("unchecked")
	public Dialog<Object> set_Active(boolean active) {
		setActive(active);
		return (Dialog<Object>) this;
	}

	@SuppressWarnings("unchecked")
	public Dialog<Object> set_Editable(boolean editable) {
		setEditable(editable);
		return (Dialog<Object>) this;
	}

	public Dialog<Object> getDialog(Object bean, String key) {
		Dialog<Object> dlg = getDialog(key);
		dlg.setCrud(true);
		dlg.setParent(this.getForm());
		dlg.setPanelPrefix(getPanelPrefix());
		dlg.setBean(bean);
		dlg.setHeader(getLocalized(key) + " / " + bean);
		return dlg;
	}

	public Dialog<Object> getDialog(String key) {
		Dialog<Object> dlg = dialogMap.get(key);
		if (dlg == null) {
			dlg = new Dialog<>();
			dialogMap.put(key, dlg);
		}
		// dlg.cleanUp();
		return dlg;
	}

	private Map<String, Dialog<Object>> dialogMap = new HashMap<>();

	public String getEditor() {
		return getEditor(getBean());
	}

	public String getEditor(Object o) {
		String ed = EditorUtils.getPanel(o, getPanelPrefix());
		return ed;
	}

	public boolean isUpdateable() {
		return isEditable() && isUpdatePermitted();
	}

	protected T bean;

	protected static final String EMPTY_SRC = "/empty.xhtml";
	protected static final String DEFAULT_UPDATE = "@this";

	protected String src, header, parent, update;

	protected String panelPrefix = "p";
	protected boolean active, panel, editable;

	protected boolean deletable = true;

	protected boolean updated;

	protected boolean crud;

	protected boolean customToolbar;

	public boolean isUpdatePermitted() {
		return true;
	}

	public void cancel() {
		cleanUp();
	}

	public void save() {
		letExtensionsWork("save");
		cleanUp();
	}

	public void delete() {
		letExtensionsWork("delete");
		cleanUp();
	}

	protected void letExtensionsWork(String key) {
		Extension ext = ExtensionRegistry.getInstance().getTopExtension(this,
				key);
		if (ext != null) {
			ext.get(this, key);
		}
	}

	protected void cleanUp() {
		setActive(false);
		setUpdated(false);
		setParent(null);
		setBean(null);
		Collection<Dialog<Object>> dialogs = dialogMap.values();
		if (dialogs != null) {
			for (Dialog<Object> d : dialogs) {
				d.cleanUp();
			}
		}
	}

	@Inject
	private Window win;

	public void listen(@Observes DialogEvent e) {
		if (win != null && win.toString().equals(e.getSourceKey())) {
			cleanUp();
		}
	}

	public String getUpdate() {
		return update != null ? update : (parent != null ? ":" + parent
				: DEFAULT_UPDATE);
	}

	public void setUpdate(String update) {
		this.update = update;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
		if (!updated) {
			uMap.clear();
		}
	}

	public String getForm() {
		return getKey() + "_form";
	}

	public String getWidget() {
		return getKey() + "_widget";
	}

	public String getKey() {
		return "d" + hashCode();
	}

	public String getSrc() {
		return !isActive() ? EMPTY_SRC : (src != null ? src : getEditor());
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public T getBean() {
		return bean;
	}

	public void setBean(T bean) {
		this.bean = bean;
		setActive(bean != null);
	}

	public boolean isCrud() {
		return crud;
	}

	public void setCrud(boolean crud) {
		this.crud = crud;
	}

	public boolean isCustomToolbar() {
		return customToolbar;
	}

	public void setCustomToolbar(boolean customToolbar) {
		this.customToolbar = customToolbar;
	}

	@Override
	public String toString() {
		return "Dialog [bean=" + bean + ", src=" + src + ", header=" + header
				+ ", active=" + active + "]";
	}

	public boolean isPanel() {
		return panel;
	}

	public void setPanel(boolean panel) {
		this.panel = panel;
	}

	public boolean isDeletable() {
		return deletable;
	}

	public void setDeletable(boolean deletable) {
		this.deletable = deletable;
	}

	public String getPanelPrefix() {
		return panelPrefix;
	}

	public void setPanelPrefix(String panelPrefix) {
		this.panelPrefix = panelPrefix;
	}

	private static final long serialVersionUID = 1L;
}
