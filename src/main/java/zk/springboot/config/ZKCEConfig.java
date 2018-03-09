package zk.springboot.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.zkoss.web.util.resource.ClassWebResource;
import org.zkoss.zk.au.http.DHtmlUpdateServlet;
import org.zkoss.zk.ui.http.HttpSessionListener;
import org.zkoss.zk.ui.http.RichletFilter;
import org.zkoss.zk.ui.http.WebManager;

import javax.annotation.PreDestroy;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class ZKCEConfig {
	private static final String UPDATE_URI = "/zkau"; //servlet mapping for ZK's update servlet
	private static final String RICHLET_URI = "/richlet";
	private static final String ZUL_VIEW_RESOLVER_PREFIX = UPDATE_URI + ClassWebResource.PATH_PREFIX + "/zul/";
	private static final String ZUL_VIEW_RESOLVER_SUFFIX = ".zul";
	private WebManager webManager;

	@Bean
	public ViewResolver zulViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver(ZUL_VIEW_RESOLVER_PREFIX, ZUL_VIEW_RESOLVER_SUFFIX);
		resolver.setOrder(InternalResourceViewResolver.LOWEST_PRECEDENCE);
		return resolver;
	}

	// allow custom UPDATE_URI configuration (other than "/zkau")
	@Bean
	public ServletContextInitializer zkWebManager() {
		return new ServletContextInitializer() {
			@Override
			public void onStartup(ServletContext servletContext) throws ServletException {
				if(WebManager.getWebManagerIfAny(servletContext) == null) {
					webManager = new WebManager(servletContext, UPDATE_URI);
				}
			}

			@PreDestroy
			public void onDestroy() {
				if(webManager != null) {
					webManager.destroy();
				}
			}
		};
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

	@Bean
	public ServletRegistrationBean dHtmlUpdateServlet() {
		return new ServletRegistrationBean(new DHtmlUpdateServlet(), UPDATE_URI + "/*");
	}

	// optional richlet filter configuration (only needed for richlets)
	@Bean
	public FilterRegistrationBean richletFilter() {
		FilterRegistrationBean reg = new FilterRegistrationBean(new RichletFilter());
		reg.addUrlPatterns(RICHLET_URI + "/*");
		return reg;
	}

	@Bean
	public HttpSessionListener httpSessionListener() {
		return new HttpSessionListener();
	}
}
