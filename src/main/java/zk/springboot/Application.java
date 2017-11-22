package zk.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import zk.springboot.config.ZKCEConfig;
import zk.springboot.config.ZKEEConfig;

@SpringBootApplication
//@Import(ZKCEConfig.class) /*ZK CE config only*/
@Import(ZKEEConfig.class) /*ZK EE config includes CE*/
public class Application  {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
