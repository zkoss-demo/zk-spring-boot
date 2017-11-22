package zk.springboot.richlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Window;
import zk.springboot.service.TestService;

public class TestRichlet extends GenericRichlet {

	@Autowired
	private TestService testService;

	@Override
	public void init(RichletConfig config) {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getWebApp().getServletContext());
	}

	@Override
	public void service(Page page) throws Exception {
		Window window = new Window("ZK-Spring-Boot Richlet", "normal", true);
		window.setPage(page);
		Button button = new Button("Get Time from Spring Service");
		button.addEventListener("onClick", event -> {
			Label label = new Label("Current Time: " + testService.getTime());
			window.appendChild(new Separator());
			window.appendChild(label);
		});
		window.appendChild(button);
	}
}
