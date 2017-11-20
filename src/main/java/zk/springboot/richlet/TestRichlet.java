package zk.springboot.richlet;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.GenericRichlet;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Richlet;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

public class TestRichlet extends GenericRichlet {

	@Override
	public void service(Page page) throws Exception {
		Window window = new Window("ZK-Spring-Boot Richlet", "normal", true);
		window.setPage(page);
	}
}
