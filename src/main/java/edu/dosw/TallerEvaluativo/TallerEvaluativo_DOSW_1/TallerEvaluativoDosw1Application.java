package edu.dosw.TallerEvaluativo.TallerEvaluativo_DOSW_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = "edu.dosw.TallerEvaluativo")
@EnableMongoRepositories(basePackages = "edu.dosw.TallerEvaluativo")
public class TallerEvaluativoDosw1Application {

	public static void main(String[] args) {
		SpringApplication.run(TallerEvaluativoDosw1Application.class, args);
	}

}
