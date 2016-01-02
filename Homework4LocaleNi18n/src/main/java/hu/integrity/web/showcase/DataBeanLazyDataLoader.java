package hu.integrity.web.showcase;

import hu.integrity.jpa.lazyload.AbstractLazyDataLoader;
import hu.integrity.jpa.lazyload.FilterOptions;
import hu.integrity.jpa.lazyload.ILazyDataLoader;
import hu.integrity.jpa.lazyload.LoadResult;
import hu.integrity.jpa.lazyload.Restrictor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Stateless
@LocalBean
public class DataBeanLazyDataLoader implements ILazyDataLoader {

	@Inject
	private EntityManager em;

	private AbstractLazyDataLoader ldl;

	@PostConstruct
	private void pc() {
		ldl = new AbstractLazyDataLoader() {

			@Override
			protected List<String> getLikeables() {
				return Arrays.asList("key");
			}

			@Override
			protected EntityManager getEM() {
				return DataBeanLazyDataLoader.this.em;
			}
		};

	}

	@Override
	public void setOptions(FilterOptions options) {
		ldl.setOptions(options);
	}

	@Override
	public <T> LoadResult<T> load(Class<T> clazz, Restrictor rest, int first,
			int pageSize, String sortField, int sortOrder,
			Map<String, String> filters, FilterOptions options) {
		return ldl.load(clazz, rest, first, pageSize, sortField, sortOrder,
				filters, options);
	}

}
