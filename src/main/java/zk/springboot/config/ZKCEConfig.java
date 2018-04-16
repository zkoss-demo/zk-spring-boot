package zk.springboot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.zkoss.web.util.resource.ClassWebResource;
import org.zkoss.zk.au.http.DHtmlUpdateServlet;
import org.zkoss.zk.ui.http.HttpSessionListener;
import org.zkoss.zk.ui.http.RichletFilter;
import org.zkoss.zk.ui.http.WebManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

public class ZKCEConfig {
	private static String UPDATE_URI = "/zkau"; //servlet mapping for ZK's update servlet
	private static final String RICHLET_URI = "/richlet"; //optional
	private static String ZUL_VIEW_RESOLVER_PREFIX = UPDATE_URI + ClassWebResource.PATH_PREFIX + "/zul/";
	private static final String ZUL_VIEW_RESOLVER_SUFFIX = ".zul";

	private WebManager webManager;

	@Bean
	public ViewResolver zulViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver(ZUL_VIEW_RESOLVER_PREFIX, ZUL_VIEW_RESOLVER_SUFFIX);
		resolver.setOrder(InternalResourceViewResolver.LOWEST_PRECEDENCE);
		return resolver;
	}

/*
	// original zk layout servlet (only for war files)
    @Bean
    public ServletRegistrationBean dHtmlLayoutServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean(new DHtmlLayoutServlet(), "*.zul");
        reg.setInitParameters(Collections.singletonMap("update-uri", UPDATE_URI));
        return reg;
    }
*/

	// optional richlet filter configuration (only needed for richlets)
	@Bean
	public FilterRegistrationBean richletFilter() {
		FilterRegistrationBean reg = new FilterRegistrationBean(new RichletFilter());
		reg.addUrlPatterns(RICHLET_URI + "/*");
		return reg;
	}

	@Bean
	@ConditionalOnClass(name="org.zkoss.zats.mimic.Zats") //Zats doesn't support custom update URI.
	public ServletRegistrationBean defaultDHtmlUpdateServlet() {
		return new ServletRegistrationBean(new DHtmlUpdateServlet(), "/zkau/*");
	}

	@Bean
	@ConditionalOnMissingClass("org.zkoss.zats.mimic.Zats") //only allow custom update URI outside Zats testcases.
	public ServletRegistrationBean customizableDHtmlUpdateServlet() {
		return new ServletRegistrationBean(new DHtmlUpdateServlet(), UPDATE_URI + "/*");
	}


	/**
	 * With Zats the listener needs to be configured in web.xml.(custom update URI isn't supported by Zats anyway).
	 * Zats runs with its own embedded Jetty.
	 */
	@Bean
	@ConditionalOnMissingClass("org.zkoss.zats.mimic.Zats") //Obsolete when using Zats
	public HttpSessionListener httpSessionListener() {
		return new HttpSessionListener() {
			private WebManager _webman;
			@Override
			public void contextInitialized(ServletContextEvent sce) {
				final ServletContext ctx = sce.getServletContext();
				if (WebManager.getWebManagerIfAny(ctx) == null) {
					_webman = new WebManager(ctx, UPDATE_URI);
				} else {
					throw new IllegalStateException("ZK WebManager already exists. Cannot initialize via Spring Boot configuration.");
				}
			}

			@Override
			public void contextDestroyed(ServletContextEvent sce) {
				if (_webman != null) {
					_webman.destroy();
				}
			}
		};
	}
}


