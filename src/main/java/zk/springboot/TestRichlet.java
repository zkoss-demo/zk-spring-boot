package zk.springboot;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.GenericRichlet;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Richlet;
import org.zkoss.zul.Label;

public class TestRichlet extends GenericRichlet {

	@Override
	public void service(Page page) throws Exception {
		//locate the zul from src/main/resources/web/zul/*.zul
		String pageName = Executions.getCurrent().getParameter("p");
		Executions.createComponents("~./zul/" + pageName + ".zul", page, null, null);
	}
}
