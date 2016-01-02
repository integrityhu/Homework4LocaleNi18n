package hu.integrity.jpa.lazyload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public abstract class AbstractLazyDataLoader implements ILazyDataLoader {

	protected FilterOptions options;
	protected String pattern;

	@Override
	public void setOptions(FilterOptions options) {
		this.options = options;
	}

	@Override
	public <T> LoadResult<T> load(Class<T> clazz, Restrictor rest, int first,
			int pageSize, String sortField, int sortOrder,
			Map<String, String> filters, FilterOptions options) {

		setOptions(options);

		filters = adjustByGlobalFilter(filters, options, clazz);

		LoadResult<T> loadResult = new LoadResult<>();

		CriteriaBuilder cb = getEM().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> from = cq.from(clazz);
		CriteriaQuery<T> select = cq.select(from);

		Predicate predicate = getWhere(rest, filters, cb, cq, from);
		if (predicate != null) {
			cq.where(predicate);
		}

		if (sortOrder != 0) {
			List<Order> orders = getOrderBy(sortField, sortOrder, cb, from);
			if (orders != null) {
				cq.orderBy(orders);
			}
		}

		TypedQuery<T> tq = getEM().createQuery(select);
		List<T> r = new ArrayList<>();

		loadResult.rowCount = getRecordCount(clazz, rest, filters).intValue();

		if (loadResult.rowCount > 0) {
			try {
				r = tq.setFirstResult(first).setMaxResults(pageSize)
						.getResultList();
			} catch (Exception e) {
				log.log(Level.WARNING, "load FAILED " + e.getMessage());
			}
		}

		loadResult.resultList = r;

		return loadResult;
	}

	private static final String GF = "globalFilter";

	private <T> Map<String, String> adjustByGlobalFilter(
			Map<String, String> filters, FilterOptions options, Class<T> clazz) {
		if (filters == null || filters.isEmpty()) {
			return filters;
		}
		String theFilter = filters.get(GF);
		if (theFilter == null || theFilter.trim().isEmpty()) {
			return filters;
		}
		options.setGlobalFilter(true);
		filters = new HashMap<>();
		List<String> fields = getLikeables();
		for (String f : fields) {
			filters.put(f, theFilter);
		}
		return filters;
	}

	private <T> List<Order> getOrderBy(String sortField, int sortOrder,
			CriteriaBuilder cb, Root<T> from) {
		List<Order> orders = null;
		if (sortField != null && isLikeableField(sortField)) {
			orders = new LinkedList<>();
			Path<T> path = from.get(sortField);
			Order o = (sortOrder > 0) ? cb.desc(path) : cb.asc(path);
			orders.add(o);
		}
		return orders;
	}

	private <T> Long getRecordCount(Class<T> clazz, Restrictor rest,
			Map<String, String> filters) {

		CriteriaBuilder cb = getEM().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<T> from = cq.from(clazz);

		cq.select(cb.count(from));

		Predicate predicate = getWhere(rest, filters, cb, cq, from);
		if (predicate != null) {
			cq.where(predicate);
		}

		Long result = null;
		try {
			result = getEM().createQuery(cq).getSingleResult();
		} catch (Exception e) {
			log.log(Level.INFO, "getRecordCount FAILED", e);
		}

		return result;

	}

	private <T> Predicate getWhere(Restrictor rest,
			Map<String, String> filters, CriteriaBuilder cb,
			CriteriaQuery<?> cq, Root<T> from) {

		List<Predicate> restrictions = new ArrayList<>();
		List<Predicate> restList = getRestrictions(rest, cb, cq, from);
		if (restList != null && !restList.isEmpty()) {
			restrictions.addAll(restList);
		}

		pattern = null;

		List<Predicate> filterRestrictions = new ArrayList<>();
		if (filters != null && !filters.isEmpty()) {
			Set<Entry<String, String>> es = filters.entrySet();
			for (Entry<String, String> filter : es) {
				String field = filter.getKey();
				if (isLikeableField(field)) {
					Expression<String> x = from.get(field);
					pattern = getPattern(filter.getValue());
					if (!options.isCaseSensitive()) {
						x = cb.lower(x);
						pattern = pattern.toLowerCase();
					}
					Predicate predicate = cb.like(x, pattern);
					filterRestrictions.add(predicate);
				}
			}
		}

		restList = getFilterRestrictions(rest, cb, from);
		if (restList != null && !restList.isEmpty()) {
			restrictions.addAll(restList);
		}

		Predicate[] predicates = restrictions.toArray(new Predicate[0]);
		Predicate primaryPredicate = restrictions.isEmpty() ? null : cb
				.and(predicates);

		Predicate[] filterPredicates = filterRestrictions
				.toArray(new Predicate[0]);
		Predicate filterPredicate = filterRestrictions.isEmpty() ? null
				: (options.isGlobalFilter() ? cb.or(filterPredicates) : cb
						.and(filterPredicates));

		Predicate predicate = null;
		if (filterPredicate == null) {
			predicate = primaryPredicate;
		} else if (primaryPredicate == null) {
			predicate = filterPredicate;
		} else {
			predicate = cb.and(primaryPredicate, filterPredicate);
		}
		return predicate;

	}

	protected <T> List<Predicate> getRestrictions(Restrictor rest,
			CriteriaBuilder cb, CriteriaQuery<?> cq, Root<T> from) {
		return null;
	}

	protected <T> List<Predicate> getFilterRestrictions(Restrictor rest,
			CriteriaBuilder cb, Root<T> from) {
		return null;
	}

	private static final String P = "%";

	private String getPattern(String value) {
		String fm = (options == null || options.getFilterMode() == null || options
				.getFilterMode().isEmpty()) ? FilterOptions.FILTER_STARTSWITH
				: options.getFilterMode();
		if (FilterOptions.FILTER_STARTSWITH.equals(fm)) {
			value = value + P;
		} else if (FilterOptions.FILTER_CONTAINS.equals(fm)) {
			value = P + value + P;
		} else if (FilterOptions.FILTER_EXACT.equals(fm)) {
			// value = value;
		} else if (FilterOptions.FILTER_ENDSWITH.equals(fm)) {
			value = P + value;
		} else if (FilterOptions.FILTER_SQLLIKE.equals(fm)) {
			// value = value;
		}
		return value;
	}

	protected abstract List<String> getLikeables();

	private boolean isLikeableField(String field) {
		boolean likeable = getLikeables().contains(field);
		if (!likeable) {
			log.fine("field '"
					+ field
					+ "' is ignored, as it can not be used in 'LIKE' or 'ORDERBY' expressions");
		}
		return likeable;
	}

	protected abstract EntityManager getEM();

	@Inject
	private Logger log;
}
