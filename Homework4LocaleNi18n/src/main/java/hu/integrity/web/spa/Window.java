package hu.integrity.web.spa;

import hu.integrity.web.dlg.DialogEvent;

import java.io.Serializable;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.WindowScoped;

@WindowScoped
@Named("win")
public class Window implements Serializable {

	@Inject
	Event<DialogEvent> events;

	public String getOnLoad() {
		events.fire(new DialogEvent(this.toString()));
		return null;
	}

	private static final long serialVersionUID = 1L;
}
