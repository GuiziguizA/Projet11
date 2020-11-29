package sid.org;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import sid.org.dao.LivreRepository;
import sid.org.dao.PretRepository;
import sid.org.dao.RolesRepository;
import sid.org.dao.UtilisateurRepository;
import sid.org.service.DateService;
import sid.org.service.LivreService;
import sid.org.service.PretService;
import sid.org.service.UtilisateurService;

@SpringBootApplication
public class BiblioAppli {
	private static final Logger logger = LoggerFactory.getLogger(BiblioAppli.class);

	public static void main(String[] args) {
		SpringApplication.run(BiblioAppli.class, args);
		logger.info("Biblio-api Started........");
	}

	@Autowired
	private RolesRepository rolesRepository;

	@Autowired
	private LivreService livreService;
	@Autowired
	private UtilisateurService utilisateurService;
	@Autowired
	private PretService pretService;
	@Autowired
	private PretRepository pretRepository;
	@Autowired
	private LivreRepository livreRepository;
	@Autowired
	private DateService dateService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UtilisateurRepository utilisateurRepository;

	/*
	 * @Override public void run(String... args) throws Exception { List<String>
	 * list = null; Roles role1 = rolesRepository.save(new Roles("user")); Roles
	 * role2 = rolesRepository.save(new Roles("employe")); Roles role3 =
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
	 * "65123"), "user");
	 * 
	 * utilisateurService.creerUtilisateur(new UtilisateurDto("batch", "batch",
	 * " adresse7", "batch", "56776"), "user"); Livre livre1 =
	 * livreService.createLivre( new LivreDto("les trois mousquetaires",
	 * "Alexandre DUMAS", "type1", "section1", "emplacement1", 1)); Livre livre2 =
	 * livreService .createLivre(new LivreDto("Bel-ami", "Guy de Maupassant",
	 * "type2", "section2", "emplacement1", 2)); Livre livre3 =
	 * livreService.createLivre( new LivreDto("le bossu de notre dame",
	 * "Henry Polac ", "type1", "section1", "emplacement1", 5));
	 * 
	 * pretService.creerPret(new Long(1), "bob@Laposte.com");
	 * pretService.creerPret(new Long(1), "michel@gmail.com");
	 * 
	 * Optional<String> opt = Optional.of("enattente");
	 * pretService.supprimerPret(new Long(2), opt);
	 * 
	 * 
	 * pretService.modifierPret(new Long(1), "remise"); pretService.creerPret(new
	 * Long(1), "marcel@gmail.com"); pretService.verifierPrêt(new Long(6));
	 * pretService.creerPret(new Long(1), "jean@gmail.com");
	 * pretService.verifierPrêt(new Long(7));
	 * 
	 * 
	 * pretService.connectApiTimer(new Long(1)); Optional<Livre> livre =
	 * livreRepository.findById(new Long(1)); if
	 * (livre.get().getListeDattente().isEmpty()) { System.out.println("ca bug"); }
	 * else { System.out.println(livre.get().getListeDattente().get(0)); }
	 * Optional<Pret> pret22 = pretRepository.findById((long) 6);
	 * System.out.println(pret22.get().getStatut()); System.out.println(
	 * "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000à"
	 * );
	 * 
	 * }
	 */

}
