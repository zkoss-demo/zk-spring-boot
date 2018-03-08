package zk.springboot.test.config;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/**
 * experimental ContextLoaderListener to load a spring boot application context using ZATS' embedded jetty
 */
public class ZatsSpringBootContextLoaderListener extends ContextLoaderListener {

	public static final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		new SpringBootServletInitializer() {
			@Override
			public void onStartup(ServletContext servletContext) {
				createRootApplicationContext(servletContext);
			}

			@Override
			protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
				return builder.sources(sce.getServletContext().getInitParameter(CONTEXT_CONFIG_LOCATION));
			}
		}.onStartup(sce.getServletContext());
	}
}
