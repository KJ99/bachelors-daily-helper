package pl.kj.bachelors.daily.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "pl.kj.bachelors")
@EnableJpaRepositories("pl.kj.bachelors.daily.infrastructure.repository")
@EntityScan("pl.kj.bachelors.daily.domain.model")
@EnableCaching
@Configuration
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}