package net.exacode.vaadin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vaadin.server.DefaultUIProvider;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class SpringUIProvider extends DefaultUIProvider {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private ApplicationContext context;

	@Override
	public UI createInstance(UICreateEvent event) {
		ensureInitialized();
		if (context == null) {
			throw new IllegalStateException("Couldn't load spring context.");
		}
		return context.getBean(event.getUIClass());
	}

	private void ensureInitialized() {
		if (context == null) {
			init();
		}
	}

	private void init() {
		VaadinServlet vaadinServlet = VaadinServlet.getCurrent();
		if (vaadinServlet == null) {
			logger.error("Couldn't get current instance of VaadinServlet");
			return;
		}
		context = WebApplicationContextUtils
				.getRequiredWebApplicationContext(vaadinServlet
						.getServletContext());
	}

}
