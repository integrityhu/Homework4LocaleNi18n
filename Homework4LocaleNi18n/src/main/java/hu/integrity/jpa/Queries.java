package hu.integrity.jpa;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public abstract class Queries {

	public abstract EntityManager getEM();

	public Long getRecordCount(Class<?> clazz) {
		CriteriaBuilder qb = getEM().getCriteriaBuilder();
		CriteriaQuery<Long> cq = qb.createQuery(Long.class);
		cq.select(qb.count(cq.from(clazz)));
		Long result = null;
		try {
			result = getEM().createQuery(cq).getSingleResult();
		} catch (Exception e) {
			log.log(Level.INFO, "getRecordCount FAILED", e);
		}
		return result;
	}

	private static Logger log = Logger.getLogger(Queries.class.getName());
}
