package com.iam.iam_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = {"com.iam.iam_app.entities", "org.wakanda.framework.entity"})
@EnableJpaRepositories(basePackages = "com.iam.iam_app.repositories")
@EnableTransactionManagement
public class IamAppApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(IamAppApplication.class, args);
	}

}
