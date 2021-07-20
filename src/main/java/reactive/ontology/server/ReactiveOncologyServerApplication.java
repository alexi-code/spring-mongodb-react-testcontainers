package reactive.ontology.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories
@SpringBootApplication
public class ReactiveOncologyServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveOncologyServerApplication.class, args);
	}

}
