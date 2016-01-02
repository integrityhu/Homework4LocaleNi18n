package hu.integrity.web.showcase;

import hu.integrity.commons.KeyFactory;
import hu.integrity.jpa.Queries;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Singleton
@Startup
public class DataFactory {

	@Inject
	private Logger log;

	@Inject
	private EntityManager em;

	@Resource
	TimerService timerService;

	@PostConstruct
	private void init() {
		timerService.createSingleActionTimer(2 * 1000, new TimerConfig(null,
				false));
	}

	@Timeout
	void onTimeout(Timer timer) {
		check();
	}

	private Queries q = new Queries() {

		@Override
		public EntityManager getEM() {
			return em;
		}
	};

	public void check() {
		Long cnt = q.getRecordCount(DataBean.class);
		if (cnt == null || cnt == 0) {
			onEmpty();
		}
	}

	private void onEmpty() {
		for (int i = 0; i < 1000; i++) {
			em.persist(createBean());
		}
	}

	private DataBean createBean() {
		DataBean bean = new DataBean();
		bean.setKey(KeyFactory.generateKey());
		return bean;
	}

	private static Random randomGenerator = new Random();

	public static String rndFromList(List<String> lst) {
		int index = randomGenerator.nextInt(lst.size());
		return lst.get(index);
	}
}
