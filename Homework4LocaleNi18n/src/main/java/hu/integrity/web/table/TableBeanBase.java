package hu.integrity.web.table;

import hu.integrity.jpa.lazyload.FilterOptions;
import hu.integrity.jpa.lazyload.ILazyDataLoader;
import hu.integrity.jpa.lazyload.LoadResult;
import hu.integrity.jpa.lazyload.Restrictor;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

public abstract class TableBeanBase<T> {

	protected T selected;

	protected FilterOptions options = new FilterOptions();

	protected abstract ILazyDataLoader getLazyDataLoader();

	private Class<?> clazz;

	protected void clean() {
		clazz = null;
		lazyResourceModel = null;
	}

	LazyDataModel<T> lazyResourceModel = null;

	public LazyDataModel<T> getDataModel() {
		if (lazyResourceModel == null) {
			lazyResourceModel = new LazyDataModel<T>() {
				private List<T> lastResult;
				
				@PostConstruct
				public void init() {
				    setPageSize(1);
				}

				@Override
				public List<T> load(int first, int pageSize, String sortField,
						SortOrder sortOrder, Map<String, String> filters) {

					filters = adjustFilters(filters);

					setSelected(null);

					@SuppressWarnings("unchecked")
					LoadResult<T> result = (LoadResult<T>) getLazyDataLoader()
							.load(getClazz(), getRestrictor(), first, pageSize,
									sortField, getSortOrder(sortOrder),
									filters, options);
					lastResult = result.resultList;
					setRowCount(result.rowCount);
					if (lastResult != null && !lastResult.isEmpty()) {
						setSelected(lastResult.get(0));
					}

					return lastResult;
				}

				private int getSortOrder(SortOrder sortOrder) {
					if (sortOrder == null
							|| sortOrder.equals(SortOrder.UNSORTED)) {
						return 0;
					}
					return sortOrder.equals(SortOrder.DESCENDING) ? 1 : -1;
				}

				@Override
				public T getRowData(String rowKey) {
					if (lastResult != null && rowKey != null
							&& !rowKey.trim().isEmpty()) {
						for (T resource : lastResult) {
							if (rowKey.equals(getRowKey(resource))) {
								return resource;
							}
						}
					}
					return null;
				}

				public Object getRowKey(T object) {
					return TableBeanBase.this.getRowKey(object);
				}

				private static final long serialVersionUID = 1L;

			};
		}
		return lazyResourceModel;
	}

	protected abstract Restrictor getRestrictor();

	protected abstract Object getRowKey(T object);

	private Map<String, String> filters;

	protected Map<String, String> adjustFilters(Map<String, String> filters) {
		this.setFilters(filters);
		return filters;
	}

	public T getSelected() {
		return selected;
	}

	public void setSelected(T selected) {
		this.selected = selected;
	}

	public FilterOptions getOptions() {
		return options;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Map<String, String> getFilters() {
		return filters;
	}

	public void setFilters(Map<String, String> filters) {
		this.filters = filters;
	}
}