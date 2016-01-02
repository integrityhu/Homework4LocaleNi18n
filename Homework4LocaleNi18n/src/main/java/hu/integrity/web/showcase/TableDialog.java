package hu.integrity.web.showcase;

import hu.integrity.jpa.lazyload.ILazyDataLoader;
import hu.integrity.jpa.lazyload.Restrictor;
import hu.integrity.web.dlg.Dialog;
import hu.integrity.web.table.TableBeanBase;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ConversationScoped;

@Named("tableDlg")
@ConversationScoped
public class TableDialog extends Dialog<TableBeanBase<DataBean>> {

	{
		src = "/showcase/tablePanel.xhtml";
		header = "table";
		editable = false;
		crud = false;
	}

	@Override
	public TableBeanBase<DataBean> getBean() {
		if (bean == null) {
			bean = new TB();
		}
		return super.getBean();
	}

	@Inject
	private DataBeanLazyDataLoader ll;

	protected class TB extends TableBeanBase<DataBean> {

		{
			setClazz(DataBean.class);
		}

		@Override
		protected ILazyDataLoader getLazyDataLoader() {
			return ll;
		}

		@Override
		protected Object getRowKey(DataBean bean) {
			return null == bean ? "-" : "" + bean.getId();
		}

		@Override
		protected Restrictor getRestrictor() {
			return null;
		}

	}

	private static final long serialVersionUID = 1L;
}
