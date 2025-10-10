package MyFirstProject.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing()
@SpringBootApplication
public class BookMyShowApplication {

	public static void main(String[] args) {

        SpringApplication.run(BookMyShowApplication.class, args);
	}

}
