package sid.org;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;




import java.util.Locale;


@SpringBootApplication

public class BatchApp {
	private static final Logger logger = LoggerFactory.getLogger(BatchApp.class);
	public static void main(String[] args) {
		SpringApplication.run(BatchApp.class, args);
		logger.info("Biblio-batch Started........");
	}


	


	

}