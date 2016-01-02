package hu.integrity.jpa.lazyload;

import java.util.Map;

/**
 * Adatbetöltés a datatable lapozási, filterezési és rendezési lehetőségeinek
 * támogatásával. Az entitás LIKE-olható mezőire automatikusan működik.
 * 
 * @author sly
 */
public interface ILazyDataLoader {

	public void setOptions(FilterOptions options);

	public <T> LoadResult<T> load(Class<T> clazz, Restrictor rest, int first,
			int pageSize, String sortField, int sortOrder,
			Map<String, String> filters, FilterOptions options);

}