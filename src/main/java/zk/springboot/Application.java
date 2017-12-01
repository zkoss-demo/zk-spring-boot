package zk.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import zk.springboot.config.ZKCEApplication;
import zk.springboot.config.ZKEEApplication;

@SpringBootApplication
//@ZKCEApplication
@ZKEEApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
