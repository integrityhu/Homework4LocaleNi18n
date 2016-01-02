package hu.integrity.web.showcase;

import hu.integrity.web.dlg.Dialog;

import javax.inject.Named;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ConversationScoped;

@Named("editDlg")
@ConversationScoped
public class EditDialog extends Dialog<Object> {

	{
		header = "are2";
		editable = true;
		crud = true;
	}

	@Override
	public Object getBean() {
		if (bean == null) {
			bean = new DataBean();
		}
		return super.getBean();
	}

	private static final long serialVersionUID = 1L;
}
