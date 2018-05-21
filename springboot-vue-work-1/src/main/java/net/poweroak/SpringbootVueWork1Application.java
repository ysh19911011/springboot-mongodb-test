package net.poweroak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SpringbootVueWork1Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootVueWork1Application.class, args);
	}
}
