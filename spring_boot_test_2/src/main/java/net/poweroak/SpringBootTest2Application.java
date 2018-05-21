package net.poweroak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;

import net.poweroak.config.servlet.CustomServlet;

@SpringBootApplication
@ServletComponentScan
public class SpringBootTest2Application {
	public static void main(String[] args) {
		SpringApplication.run(SpringBootTest2Application.class, args);
	}
}
