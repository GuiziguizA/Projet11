package sid.org.biblio.front;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.RestTemplate;


import sid.org.biblio.front.classe.LivreCriteria;
import sid.org.biblio.front.classe.Pret;
import sid.org.biblio.front.classe.Sessions;
import sid.org.biblio.front.classe.Utilisateur;
import sid.org.biblio.front.config.SimpleAuthenticationFilter;
import sid.org.biblio.front.config.UserDetailsImpl;
import sid.org.biblio.front.service.BookService;
import sid.org.biblio.front.service.PretService;
import sid.org.biblio.front.service.UtilisateurService;

@SpringBootApplication

public class Application{
	 private static final Logger logger = LoggerFactory.getLogger(Application.class);
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		logger.info("Biblio-front Started........");
	}

	
}
