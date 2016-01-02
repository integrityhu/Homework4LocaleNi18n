package hu.integrity.web.dlg;


public class DialogEvent {
	private String sourceKey;

	public DialogEvent(String sourceKey) {
		this.setSourceKey(sourceKey);
	}

	public String getSourceKey() {
		return sourceKey;
	}

	public void setSourceKey(String sourceKey) {
		this.sourceKey = sourceKey;
	}

}
