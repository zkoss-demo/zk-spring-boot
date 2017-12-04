package zk.springboot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "zk.ee")
public class ZkEEProperties {
	private boolean websocketsEnabled = false;

	public boolean isWebsocketsEnabled() {
		return websocketsEnabled;
	}

	public void setWebsocketsEnabled(boolean websocketsEnabled) {
		this.websocketsEnabled = websocketsEnabled;
	}
}
