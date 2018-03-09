package zk.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import zk.springboot.config.ZKCEApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import zk.springboot.config.ZKEEApplication;

@SpringBootApplication
//@ZKCEApplication
@ZKEEApplication
@Controller
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@GetMapping("/mvvm")
	public String mvvmExample() {
		return "mvvm";
	}

	@GetMapping("/resources")
	public String resourcesExample() {
		return "resources";
	}
}
