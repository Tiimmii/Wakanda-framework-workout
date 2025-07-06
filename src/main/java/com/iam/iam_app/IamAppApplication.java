package com.iam.iam_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EntityScan(basePackages = {"com.iam.iam_app.entity", "org.wakanda.framework.entity"})
@ConfigurationPropertiesScan("com.iam.iam_app.config")
@ComponentScan(basePackages = {"com.iam.iam_app"})
@EnableJpaRepositories
public class IamAppApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(IamAppApplication.class, args);
	}

}
