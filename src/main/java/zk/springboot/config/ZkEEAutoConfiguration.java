package zk.springboot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zkoss.zkmax.Version;
import org.zkoss.zkmax.au.websocket.WebSocketFilter;
import org.zkoss.zkmax.ui.comet.CometAsyncServlet;

@Configuration
@ConditionalOnClass({Version.class})
@EnableConfigurationProperties(ZkEEProperties.class)
public class ZkEEAutoConfiguration {
	public static final String COMET_ASYNC_INSTALLED = "org.zkoss.zkmax.ui.comet.async.installed";
	private final ZkEEProperties zkProperties;

	public ZkEEAutoConfiguration(ZkEEProperties zkProperties) {
		this.zkProperties = zkProperties;
	}

	@Bean
	public ServletContextInitializer manualServletConfigInit() {
		return servletContext -> {
			//required to avoid duplicate installing of the CometAsyncServlet
			//startup sequence in spring boot is different to a normal servlet webapp
			servletContext.setAttribute(COMET_ASYNC_INSTALLED, true);
			//servletContext.setAttribute("org.zkoss.zkmax.ws.filter.installed", true); //when FR ZK-3799 is ready (8.5.1 ?)
		};
	}

	@Bean
	public ServletRegistrationBean cometAsyncServlet() {
		ServletRegistrationBean reg = new ServletRegistrationBean(new CometAsyncServlet(), "/zkcomet/*");
		reg.setAsyncSupported(true);
		return reg;
	}

	//optional: use when websockets are enabled in zk.xml
	@Bean
	@ConditionalOnProperty(prefix = "zk.ee", name = "websockets-enabled")
	public FilterRegistrationBean wsFilter() {
		FilterRegistrationBean reg = new FilterRegistrationBean(new WebSocketFilter());
		reg.addUrlPatterns(SpringBootWebSocketWebAppInit.getWebSocketUrl() + "/*");
		//reg.addUrlPatterns(WebSocketWebAppInit.getWebSocketUrl() + "/*"); //when FR ZK-3799 is ready (8.5.1 ?)
		return reg;
	}
}
