package zk.springboot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zkoss.web.util.resource.ClassWebResource;
import org.zkoss.zk.au.http.DHtmlUpdateServlet;
import org.zkoss.zk.ui.http.DHtmlLayoutServlet;
import org.zkoss.zk.ui.http.HttpSessionListener;
import org.zkoss.zk.ui.http.RichletFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Configuration
@EnableConfigurationProperties(ZkProperties.class)
public class ZkAutoConfiguration {

	private final ZkProperties zkProperties;

	public ZkAutoConfiguration(ZkProperties zkProperties) {
		this.zkProperties = zkProperties;
	}

	// forward zul files to update/resource servlet (only for jar deployment)
	@Controller
	@ConditionalOnProperty(prefix = "zk", name = "use-layout-servlet", matchIfMissing = true)
	public class ZulForwardController {
		private String zulForwardUri = zkProperties.getUpdateUri().replace("/*", "")
				+ ClassWebResource.PATH_PREFIX + zkProperties.getZulPath();

		@RequestMapping(value = "/**/*.zul")
		public String handleTestRequest(HttpServletRequest request) {
			return "forward:" + zulForwardUri + request.getServletPath();
		}
	}

	// original zk layout servlet (only for war files)
	@Bean
	@ConditionalOnProperty(prefix = "zk", name = "use-layout-servlet", havingValue = "false", matchIfMissing = false)
	public ServletRegistrationBean dHtmlLayoutServlet() {
		ServletRegistrationBean reg = new ServletRegistrationBean(new DHtmlLayoutServlet(), "*.zul");
		reg.setInitParameters(Collections.singletonMap("update-uri", zkProperties.getUpdateUri().replace("/*", "")));
		return reg;
	}

	@Bean
	public ServletRegistrationBean dHtmlUpdateServlet() {
		return new ServletRegistrationBean(new DHtmlUpdateServlet(), zkProperties.getUpdateUri());
	}

	// optional richlet filter configuration (only needed for richlets)
	@Bean
	@ConditionalOnProperty(prefix = "zk", name = "richlet-enabled")
	public FilterRegistrationBean richletFilter() {
		FilterRegistrationBean reg = new FilterRegistrationBean(new RichletFilter());
		reg.addUrlPatterns(zkProperties.getRichletUri());
		return reg;
	}

	@Bean
	public HttpSessionListener httpSessionListener() {
		return new HttpSessionListener();
	}
}
