package hu.integrity.jpa.lazyload;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class FilterOptions implements Serializable {

	public static String FILTER_STARTSWITH = "startsWith";
	public static String FILTER_CONTAINS = "contains";
	public static String FILTER_EXACT = "exact";
	public static String FILTER_ENDSWITH = "endsWith";
	public static String FILTER_SQLLIKE = "sqlLIKE";

	private static List<String> FILTERMODES = Arrays.asList(FILTER_STARTSWITH,
			FILTER_CONTAINS, FILTER_EXACT, FILTER_ENDSWITH, FILTER_SQLLIKE);

	private String filterMode = FILTERMODES.get(0);
	private boolean caseSensitive, globalFilter;

	public String getFilterMode() {
		return filterMode;
	}

	public void setFilterMode(String filterMode) {
		this.filterMode = filterMode;
	}

	public List<String> getFilterModes() {
		return FILTERMODES;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public boolean isGlobalFilter() {
		return globalFilter;
	}

	public void setGlobalFilter(boolean globalFilter) {
		this.globalFilter = globalFilter;
	}

	private static final long serialVersionUID = 1L;
}