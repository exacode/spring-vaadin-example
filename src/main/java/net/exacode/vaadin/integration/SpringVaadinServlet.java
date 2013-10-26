package net.exacode.vaadin.integration;

import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;

@SuppressWarnings("serial")
public class SpringVaadinServlet extends VaadinServlet implements
		SessionInitListener {

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		getService().addSessionInitListener(this);
	}

	@Override
	public void sessionInit(SessionInitEvent event) throws ServiceException {
		VaadinSession session = event.getSession();
		List<UIProvider> defaultUiProviders = event.getSession()
				.getUIProviders();
		for (UIProvider uiProvider : defaultUiProviders) {
			session.removeUIProvider(uiProvider);
		}
		session.addUIProvider(new SpringUIProvider());
	}
}
