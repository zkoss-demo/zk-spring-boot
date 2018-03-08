package zk.springboot.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zkoss.web.util.resource.ClassWebResource;
import org.zkoss.zk.au.http.DHtmlUpdateServlet;
import org.zkoss.zk.ui.http.HttpSessionListener;
import org.zkoss.zk.ui.http.RichletFilter;
import org.zkoss.zk.ui.http.WebManager;

import javax.annotation.PreDestroy;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public class ZKCEConfig {
	private static final String UPDATE_URI = "/zkau"; //servlet mapping for ZK's update servlet
	private static final String RICHLET_URI = "/richlet";
	private static final String ZUL_FORWARD_URI = UPDATE_URI + ClassWebResource.PATH_PREFIX + "/zul";
	private WebManager webManager;


	// TODO: use ViewResolver ...
	// forward zul files to update/resource servlet (only for jar deployment)
	@Controller
	public class ZulForwardController {
		@RequestMapping(value = "/**/*.zul")
		public String handleTestRequest(HttpServletRequest request) {
			return "forward:" + ZUL_FORWARD_URI + request.getServletPath();
		}
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
