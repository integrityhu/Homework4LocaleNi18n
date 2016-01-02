package hu.integrity.web.spa;

import hu.integrity.extp.ClassNamePanelResolver;
import hu.integrity.extp.ExtensionRegistry;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Named;

@Singleton
@Startup
@Named("spa")
public class SPA {

	@PostConstruct
	private void pc() {
		ExtensionRegistry.getInstance().addExtension(
				new ClassNamePanelResolver());
	}

	public String getVersion() {
		return "v1.1.1.1";
	}
}
