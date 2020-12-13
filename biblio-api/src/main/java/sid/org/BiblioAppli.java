package sid.org;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BiblioAppli {
	private static final Logger logger = LoggerFactory.getLogger(BiblioAppli.class);

	public static void main(String[] args) {
		SpringApplication.run(BiblioAppli.class, args);
		logger.info("Biblio-api Started........");
	}

	/*
	 * @Autowired private RolesRepository rolesRepository;
	 * 
	 * @Autowired private LivreService livreService;
	 * 
	 * @Autowired private UtilisateurService utilisateurService;
	 * 
	 * @Autowired private PretService pretService;
	 * 
	 * @Autowired private PretRepository pretRepository;
	 * 
	 * @Autowired private LivreRepository livreRepository;
	 * 
	 * @Autowired private DateService dateService;
	 * 
	 * @Autowired private PasswordEncoder passwordEncoder;
	 * 
	 * @Autowired private UtilisateurRepository utilisateurRepository;
	 * 
	 * @Override public void run(String... args) throws Exception {
	 * 
	 * Roles role1 = rolesRepository.save(new Roles("user")); Roles role2 =
	 * rolesRepository.save(new Roles("employe")); Roles role3 =
	 * rolesRepository.save(new Roles("admin")); Utilisateur admin =
	 * utilisateurService.creerUtilisateur( new UtilisateurDto("admin",
	 * "admin@gmail.com", " 30 rue du Lac", "admin", "93450"), "admin"); Utilisateur
	 * employe = utilisateurService.creerUtilisateur( new UtilisateurDto("Jean",
	 * "jean@gmail.com", "25 rue de la rose", "employe", "94520"), "employe");
	 * Utilisateur user1 = utilisateurService.creerUtilisateur( new
	 * UtilisateurDto("Michel", "michel@gmail.com", "34 rue de chretiente",
	 * "michel", "28450"), "user"); Utilisateur user2 =
	 * utilisateurService.creerUtilisateur( new UtilisateurDto("Bob",
	 * "bob@laposte.com", "8rue de le scandinavie", "bob", "85641"), "user");
	 * Utilisateur user3 = utilisateurService.creerUtilisateur( new
	 * UtilisateurDto("Marcel", "marcel@gmail.com", "14 avenue du chene", "marcel",
	 * "65123"), "user"); Utilisateur user4 = utilisateurService.creerUtilisateur(
	 * new UtilisateurDto("Marcel", "julien@gmail.com", "14 avenue du chene",
	 * "julien", "65123"), "user");
	 * 
	 * utilisateurService.creerUtilisateur(new UtilisateurDto("batch", "batch",
	 * " adresse7", "batch", "56776"), "user"); Livre livre1 =
	 * livreService.createLivre( new LivreDto("les trois mousquetaires",
	 * "Alexandre DUMAS", "type1", "section1", "emplacement1", 1)); Livre livre2 =
	 * livreService .createLivre(new LivreDto("Bel-ami", "Guy de Maupassant",
	 * "type2", "section2", "emplacement1", 1)); Livre livre3 =
	 * livreService.createLivre( new LivreDto("le bossu de notre dame",
	 * "Henry Polac ", "type1", "section1", "emplacement1", 1));
	 * 
	 * }
	 */

}
