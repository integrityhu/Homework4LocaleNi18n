package hu.integrity.web.spa;

import java.net.MalformedURLException;
import java.net.URL;

import javax.faces.view.facelets.ResourceResolver;

public class SPAResourceResolver extends ResourceResolver {
	private ResourceResolver parent;
	private String basePath;

	public SPAResourceResolver(ResourceResolver parent) {
		this();
		this.parent = parent;
	}

	public SPAResourceResolver() {
		this.basePath = "WEB-INF/classes/META-INF/resources";
	}

	@Override
	public URL resolveUrl(String path) {
		URL url = null;

		if (parent != null) {
			url = parent.resolveUrl(path); // Resolves from WAR.
		}

		if (url == null) {
			String u = parent.resolveUrl("/").toExternalForm();
			u = u + basePath + path;
			try {
				url = new URL(u);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		return url;
	}

}
