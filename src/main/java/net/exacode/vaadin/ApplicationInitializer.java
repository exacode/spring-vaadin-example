package net.exacode.vaadin;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import net.exacode.vaadin.config.ApplicationConfiguration;
import net.exacode.vaadin.config.ApplicationProfiles;
import net.exacode.vaadin.ui.HelloWorldUi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextCleanupListener;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.vaadin.server.VaadinServlet;

public class ApplicationInitializer implements WebApplicationInitializer {

	private static final String ENVIRONMENT_FILE = "classpath:/environment.properties";

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void onStartup(ServletContext servletContext)
			throws ServletException {
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();

		loadEnvironmentProperties(rootContext.getEnvironment(),
				ENVIRONMENT_FILE);
		logger.info("Spring active profiles: {}", Arrays.toString(rootContext
				.getEnvironment().getActiveProfiles()));

		rootContext.register(ApplicationConfiguration.class);
		registerSpringContextLoaderListener(servletContext, rootContext);
		registerVaadinServlet(servletContext, rootContext);
	}

	private void loadEnvironmentProperties(
			ConfigurableEnvironment configurableEnvironment, String fileName) {
		try {
			configurableEnvironment.getPropertySources().addLast(
					new ResourcePropertySource(fileName));
		} catch (IOException e) {
			// Properties file cannot be found.
			// We cannot ignore it!
			throw new IllegalStateException(
					"Unable to load environment properties: " + fileName
							+ ". Error: " + e.getMessage());
		}
	}

	private void registerVaadinServlet(ServletContext servletContext,
			AnnotationConfigWebApplicationContext rootContext) {
		boolean productionProfile = Arrays.asList(
				rootContext.getEnvironment().getActiveProfiles()).contains(
				ApplicationProfiles.PRODUCTION);
		if (productionProfile) {
			logger.info("Application started in production profile");
		}

		VaadinServlet vaadinServlet = new VaadinServlet();
		ServletRegistration.Dynamic vaadinServletRegistration = servletContext
				.addServlet("vaadinServlet", vaadinServlet);
		vaadinServletRegistration.setInitParameter("ui",
				HelloWorldUi.class.getName());
		vaadinServletRegistration.setInitParameter("productionMode",
				Boolean.toString(productionProfile));
		vaadinServletRegistration.setInitParameter("UIProvider",
				SpringUIProvider.class.getName());
		vaadinServletRegistration.setLoadOnStartup(1);
		vaadinServletRegistration.addMapping("/*");

	}

	private void registerSpringContextLoaderListener(
			ServletContext servletContext,
			AnnotationConfigWebApplicationContext rootContext) {
		servletContext.addListener(new ContextLoaderListener(rootContext));
		servletContext.addListener(new ContextCleanupListener());
		servletContext.addListener(new RequestContextListener());
	}

}
