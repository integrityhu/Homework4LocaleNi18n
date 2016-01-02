package hu.integrity.extp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ExtensionRegistry {
	private static ExtensionRegistry _INSTANCE = new ExtensionRegistry();

	private ExtensionRegistry() {
	};

	public static ExtensionRegistry getInstance() {
		return _INSTANCE;
	}

	private List<Extension> registry = new ArrayList<>();

	public Extension getTopExtension(final Object o, final String ns) {
		List<Extension> ee = ExtensionRegistry.getInstance().getExtensions(o,
				ns);
		return (ee == null || ee.isEmpty()) ? null : ee.get(0);
	}

	public List<Extension> getExtensions(final Object o, final String ns) {
		List<Extension> matching = new ArrayList<>();
		for (Extension e : registry) {
			if (e.match(o, ns)) {
				matching.add(e);
			}
		} /*
		Collections.sort(matching, new Comparator<Extension>() {

			@Override
			public int compare(Extension e1, Extension e2) {
				return Integer.compare(getRank(e1), getRank(e2));
			}

			private int getRank(Extension e) {
				return e == null ? 0 : e.getRank(o, ns);
			}
		});
		*/
		return matching;
	}

	public void addExtension(Extension ext) {
		registry.add(ext);
	}
}
