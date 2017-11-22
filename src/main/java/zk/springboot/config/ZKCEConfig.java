package zk.springboot.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zkoss.web.util.resource.ClassWebResource;
import org.zkoss.zk.au.http.DHtmlUpdateServlet;
import org.zkoss.zk.ui.http.HttpSessionListener;
import org.zkoss.zk.ui.http.RichletFilter;

import javax.servlet.http.HttpServletRequest;

public class ZKCEConfig {
	private static final String UPDATE_URI = "/zkau"; //servlet mapping for ZK's update servlet
	private static final String RICHLET_URI = "/richlet";
	private static final String ZUL_FORWARD_URI = UPDATE_URI + ClassWebResource.PATH_PREFIX  + "/zul";

	// forward zul files to update/resource servlet (only for jar deployment)
	@Controller
	public class ZulForwardController {
		@RequestMapping(value = "/**/*.zul")
		public String handleTestRequest(HttpServletRequest request) {
			return "forward:" + ZUL_FORWARD_URI + request.getServletPath();
		}
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
