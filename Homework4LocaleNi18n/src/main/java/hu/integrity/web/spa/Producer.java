package hu.integrity.web.spa;

import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class Producer {

	@PersistenceContext
	private EntityManager em;

	@Produces
	public EntityManager produceEntityManager(InjectionPoint injectionPoint) {
		return em;
	}

	@Produces
	public Logger produceLog(InjectionPoint injectionPoint) {
		return Logger.getLogger(injectionPoint.getMember().getDeclaringClass()
				.getName());
	}

	@Produces
	@RequestScoped
	public FacesContext produceFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(Producer.class.getName());
}
