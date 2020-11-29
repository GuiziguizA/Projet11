package sid.org.service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class UtilisateurServiceTestContextConfiguration {

	@Bean
	public UtilisateurService UtilisateurService() {
		return new UtilisateurServiceImpl();
	}

}
