package sid.org.biblio.front;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import sid.org.biblio.front.service.PretService;

@SpringBootApplication

public class Application implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		logger.info("Biblio-front Started........");
	}

	@Autowired
	private PretService pretService;

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub

		/*
		 * pretService.supprimerPret(new Long(1), "michel@gmail.com", "michel",
		 * "encours");
		 */

	}

}
