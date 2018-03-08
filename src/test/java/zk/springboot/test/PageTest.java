package zk.springboot.test;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.zkoss.zats.junit.AutoClient;
import org.zkoss.zats.junit.AutoEnvironment;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zul.Label;

import java.util.function.Supplier;

public class PageTest {
	@ClassRule
	public static AutoEnvironment env = new AutoEnvironment("src/test/webapp/WEB-INF", "src/test/webapp");

	@Rule
	public AutoClient client = env.autoClient();

	private DesktopAgent desktopAgent;

	@Test
	public void testRender() {
		desktopAgent = client.connect("/mvvm.zul");
		Assert.assertNotNull(firstButton());
		Assert.assertNotNull(secondButton());
		Assert.assertNull(testLabel());
	}


	@Test
	public void testButtons() {
		desktopAgent = client.connect("/mvvm.zul");
		firstButton().click();
		Assert.assertEquals("some data for page 1 (could be a more complex object)", testLabel().as(Label.class).getValue());
		secondButton().click();
		Assert.assertEquals("different data for page 2", testLabel().as(Label.class).getValue());
	}

	private ComponentAgent testLabel() {
		return desktopAgent.query("window window label");
	}

	private ComponentAgent firstButton() {
		return desktopAgent.query("button");
	}

	private ComponentAgent secondButton() {
		return firstButton().getNextSibling();
	}
}
