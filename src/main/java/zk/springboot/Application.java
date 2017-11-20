package zk.springboot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zkoss.zk.au.http.DHtmlUpdateServlet;
import org.zkoss.zk.ui.http.HttpSessionListener;
import org.zkoss.zk.ui.http.RichletFilter;
import org.zkoss.zkmax.au.websocket.WebSocketFilter;
import org.zkoss.zkmax.ui.comet.CometAsyncServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
public class Application implements ServletContextInitializer {

	private final String UPDATE_URI = "/zkau";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		//required to avoid duplicate installing of the CometAsyncServlet
		//startup sequence in spring boot is different to a normal servlet webapp
		servletContext.setAttribute("org.zkoss.zkmax.ui.comet.async.installed", true);
//        servletContext.setAttribute("org.zkoss.zkmax.ws.filter.installed", true); //when FR ZK-3799 is ready (8.5.1 ?)
	}

	// EE-Only
	@Bean
	public ServletRegistrationBean cometAsyncServlet() {
		ServletRegistrationBean reg = new ServletRegistrationBean(new CometAsyncServlet(), "/zkcomet/*");
		reg.setAsyncSupported(true);
		reg.setLoadOnStartup(1);
		return reg;
	}

/*
	// original zk layout servlet (only for war files)
    @Bean
    public ServletRegistrationBean dHtmlLayoutServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean(new DHtmlLayoutServlet(), "*.zul");
        reg.setInitParameters(Collections.singletonMap("update-uri", UPDATE_URI));
        reg.setLoadOnStartup(2);
        return reg;
    }
*/

	// alternative: forward zul files to update/resource servlet (for jar deployment)
	@Controller
	public class ZulForwardController {
		@RequestMapping(value = "/**/*.zul")
		public String handleTestRequest(HttpServletRequest request) {
			return "forward:" + UPDATE_URI + "/web/zul" + request.getServletPath();
		}
	}

	@Bean
	public ServletRegistrationBean dHtmlUpdateServlet() {
		ServletRegistrationBean reg = new ServletRegistrationBean(new DHtmlUpdateServlet(), UPDATE_URI + "/*");
		reg.setLoadOnStartup(3);
		return reg;
	}

	@Bean
	public FilterRegistrationBean richletFilter() {
		FilterRegistrationBean reg = new FilterRegistrationBean(new RichletFilter());
		reg.addUrlPatterns("/richlet/*");
		return reg;
	}

	// EE-Only
	@Bean
	public FilterRegistrationBean wsFilter() {
		FilterRegistrationBean reg = new FilterRegistrationBean(new WebSocketFilter());
		reg.addUrlPatterns(SpringBootWebSocketWebAppInit.getWebSocketUrl() + "/*");
		//reg.addUrlPatterns(WebSocketWebAppInit.getWebSocketUrl() + "/*"); //when FR ZK-3799 is ready (8.5.1 ?)
		return reg;
	}

	@Bean
	public HttpSessionListener httpSessionListener() {
		return new HttpSessionListener();
	}
}
