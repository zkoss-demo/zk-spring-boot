package zk.springboot.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.zkoss.zkmax.au.websocket.WebSocketFilter;
import org.zkoss.zkmax.au.websocket.WebSocketWebAppInit;
import org.zkoss.zkmax.ui.comet.CometAsyncServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@Import(ZKCEConfig.class)
public class ZKEEConfig {
	@Bean
	public ServletContextInitializer manualServletConfigInit () {
		return servletContext -> {
			//required to avoid duplicate installing of the CometAsyncServlet
			//startup sequence in spring boot is different to a normal servlet webapp
			servletContext.setAttribute("org.zkoss.zkmax.ui.comet.async.installed", true);
	        servletContext.setAttribute("org.zkoss.zkmax.au.websocket.filter.installed", true);
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
	public FilterRegistrationBean wsFilter() {
		FilterRegistrationBean reg = new FilterRegistrationBean(new WebSocketFilter());
		reg.addUrlPatterns(WebSocketWebAppInit.getWebSocketUrl() + "/*");
		return reg;
	}
}
