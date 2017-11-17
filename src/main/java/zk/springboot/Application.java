package zk.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zkoss.zk.au.http.DHtmlUpdateServlet;
import org.zkoss.zk.ui.http.DHtmlLayoutServlet;
import org.zkoss.zk.ui.http.HttpSessionListener;
import org.zkoss.zkmax.au.websocket.WebSocketFilter;
import org.zkoss.zkmax.ui.comet.CometAsyncServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.HashMap;
import java.util.Map;

//@Configuration
@SpringBootApplication
public class Application implements ServletContextInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        //required to avoid duplicate installing of the CometAsyncServlet
        //startup sequence in spring boot is different to a normal servlet webapp
        servletContext.setAttribute("org.zkoss.zkmax.ui.comet.async.installed", true);
    }

    /*
	 * ZK servlets
	 */
    @Bean
    public ServletRegistrationBean cometAsyncServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean(new CometAsyncServlet(), "/zkcomet/*");
        reg.setAsyncSupported(true);
        reg.setLoadOnStartup(1);
        return reg;
    }

    @Bean
    public ServletRegistrationBean dHtmlLayoutServlet() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("update-uri", "/zkau");
        DHtmlLayoutServlet dHtmlLayoutServlet = new DHtmlLayoutServlet();
        ServletRegistrationBean reg = new ServletRegistrationBean(dHtmlLayoutServlet, "*.zul", "/zk/*");
        reg.setLoadOnStartup(2);
        reg.setInitParameters(params);
        return reg;
    }

    @Bean
    public ServletRegistrationBean dHtmlUpdateServlet() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("update-uri", "/zkau/*");
        ServletRegistrationBean reg = new ServletRegistrationBean(new DHtmlUpdateServlet(), "/zkau/*");
        reg.setLoadOnStartup(3);
        reg.setInitParameters(params);
        return reg;
    }

    @Bean
    public FilterRegistrationBean wsFilter() {
        FilterRegistrationBean reg = new FilterRegistrationBean(new WebSocketFilter());
        reg.addUrlPatterns(SpringBootWebSocketWebAppInit.getWebSocketUrl() + "/*");
        return reg;
    }

    @Bean
    public HttpSessionListener httpSessionListener() {
        return new HttpSessionListener();
    }
}