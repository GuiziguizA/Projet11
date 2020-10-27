package sid.org;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import sid.org.batch.PretBatchService;

@SpringBootApplication

public class BatchApp implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(BatchApp.class);

	public static void main(String[] args) {
		SpringApplication.run(BatchApp.class, args);
		logger.info("Biblio-batch Started........");
	}

	@Autowired
	private PretBatchService pretBatchService;

	@Override
	public void run(String... args) throws Exception {

		pretBatchService.TimerDisponibiliteLivre(new Long(1));

	}

}