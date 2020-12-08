package sid.org.service;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import sid.org.classe.Livre;
import sid.org.classe.Pret;
import sid.org.classe.Roles;
import sid.org.classe.Utilisateur;
import sid.org.dao.LivreRepository;
import sid.org.dao.PretRepository;
import sid.org.dao.UtilisateurRepository;
import sid.org.exception.BadException;
import sid.org.exception.EntityAlreadyExistException;
import sid.org.exception.LivreIndisponibleException;
import sid.org.exception.ResultNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
public class PretServiceTest {

	@Autowired
	private PretService pretService;
	@MockBean
	private PretRepository pretRepository;
	@MockBean
	private LivreRepository livreRepository;
	@Autowired
	private DateService dateService;
	@MockBean
	private UtilisateurRepository utilisateurRepository;
	@Autowired
	private EmailService emailService;
	@Autowired
	private HttpService httpService;
	@MockBean
	private RestTemplate restTemplate;
	@MockBean
	private ConnectionApiService connectionApiService;

	@Test
	public void TrouverPretEncours() {
		List<Pret> list = new ArrayList<Pret>();

		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);
		Pret pret1 = new Pret(1L, new Date(), new Date(), "remis", 1, livre, utilisateur);
		Pret pret2 = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);

		list.add(pret);
		list.add(pret1);
		list.add(pret1);

		Optional<Pret> pretEnCours = pretService.trouverPretenCours(list);

		assertEquals(pretEnCours.get().getStatut(), "encours");

	}

	@Test
	public void TrouverPretEncourslisteVide() {
		List<Pret> list = new ArrayList<Pret>();

		Optional<Pret> pretEnCours = pretService.trouverPretenCours(list);

		assertEquals(pretEnCours, Optional.empty());

	}

	@Test
	public void afficherLesPrets() throws ResultNotFoundException {
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		List<Pret> list = new ArrayList<Pret>();
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);
		Pret pret1 = new Pret(1L, new Date(), new Date(), "remis", 1, livre, utilisateur);
		Pret pret2 = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);

		list.add(pret);
		list.add(pret1);
		list.add(pret1);

		Page<Pret> pagePrets = new PageImpl<Pret>(list);

		Mockito.when(utilisateurRepository.findByMail(Mockito.anyString())).thenReturn(Optional.of(utilisateur));

		Mockito.when(pretRepository.findByUtilisateur(Mockito.any(Utilisateur.class), Mockito.any(Pageable.class)))
				.thenReturn(pagePrets);

		Page<Pret> page = pretService.afficherPrets("bob", 2, 2);

		assertEquals(page.getSize(), 3);

	}

	@Test
	public void afficherLesPretsSizeNull() throws ResultNotFoundException {
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		List<Pret> list = new ArrayList<Pret>();
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);
		Pret pret1 = new Pret(1L, new Date(), new Date(), "remis", 1, livre, utilisateur);
		Pret pret2 = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);

		list.add(pret);
		list.add(pret1);
		list.add(pret1);

		Page<Pret> pagePrets = new PageImpl<Pret>(list);

		Mockito.when(utilisateurRepository.findByMail(Mockito.anyString())).thenReturn(Optional.of(utilisateur));

		Mockito.when(pretRepository.findByUtilisateur(Mockito.any(Utilisateur.class), Mockito.any(Pageable.class)))
				.thenReturn(pagePrets);

		ResultNotFoundException exception = assertThrows(ResultNotFoundException.class, () -> {
			Page<Pret> page = pretService.afficherPrets("bob", 2, 0);

		});

		String expectedMessage = "le parametre size est incorrect";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void afficherLesPretsExceptionUtilisateurNotExist() throws ResultNotFoundException {
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		List<Pret> list = new ArrayList<Pret>();
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);
		Pret pret1 = new Pret(1L, new Date(), new Date(), "remis", 1, livre, utilisateur);
		Pret pret2 = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);

		list.add(pret);
		list.add(pret1);
		list.add(pret1);

		Page<Pret> pagePrets = new PageImpl<Pret>(list);

		Mockito.when(utilisateurRepository.findByMail(Mockito.anyString())).thenReturn(Optional.empty());

		Mockito.when(pretRepository.findByUtilisateur(Mockito.any(Utilisateur.class), Mockito.any(Pageable.class)))
				.thenReturn(pagePrets);

		ResultNotFoundException exception = assertThrows(ResultNotFoundException.class, () -> {
			Page<Pret> page = pretService.afficherPrets("bob", 2, 2);

		});

		String expectedMessage = "l'utilisateur est introuvable";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void afficherPrets() throws ResultNotFoundException {
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		List<Pret> list = new ArrayList<Pret>();
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);
		Pret pret1 = new Pret(1L, new Date(), new Date(), "remis", 1, livre, utilisateur);
		Pret pret2 = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);

		list.add(pret);
		list.add(pret1);
		list.add(pret1);

		Mockito.when(pretRepository.findAll()).thenReturn(list);

		List<Pret> listPrets = pretService.afficherPrets();

		assertEquals(listPrets.size(), 3);

	}

	@Test
	public void afficherPretsEnFontionStatut() throws ResultNotFoundException {
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		List<Pret> list = new ArrayList<Pret>();
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);
		Pret pret1 = new Pret(1L, new Date(), new Date(), "remis", 1, livre, utilisateur);
		Pret pret2 = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);

		list.add(pret);
		list.add(pret1);
		list.add(pret1);

		Mockito.when(pretRepository.findByStatut(Mockito.anyString())).thenReturn(list);

		List<Pret> listPrets = pretService.afficherPrets("encours");

		assertEquals(listPrets.size(), 3);

	}

	@Test
	public void afficherUnPret() throws ResultNotFoundException {
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);

		Mockito.when(pretRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(pret));

		Pret pret1 = pretService.afficherUnPret(1L);

		assertEquals(pret1.getLivre().getNom(), "les comptes");

	}

	@Test
	public void afficherUnPretExceptionNotFound() throws ResultNotFoundException {
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);

		Mockito.when(pretRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		ResultNotFoundException exception = assertThrows(ResultNotFoundException.class, () -> {
			Pret pret1 = pretService.afficherUnPret(1L);

		});

		String expectedMessage = "ce pret n'existe pas";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void modifierStatutPretExceptionNotFound() throws ResultNotFoundException {
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);
		Date date = Mockito.mock(Date.class);

		Mockito.when(pretRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		Mockito.when(pretRepository.saveAndFlush(Mockito.any(Pret.class))).thenReturn(pret);

		ResultNotFoundException exception = assertThrows(ResultNotFoundException.class, () -> {
			pretService.modifierStatut(1L);

		});

		String expectedMessage = "le pret n'existe pas";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void modifierStatutPret() throws ResultNotFoundException {
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);
		Date date = Mockito.mock(Date.class);

		Mockito.when(pretRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(pret));
		Mockito.when(pretRepository.saveAndFlush(Mockito.any(Pret.class))).thenReturn(pret);

		pretService.modifierStatut(1L);

	}

	@Test
	public void creerUnPret()
			throws ResultNotFoundException, LivreIndisponibleException, EntityAlreadyExistException, BadException {

		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);
		List<Pret> list = new ArrayList<Pret>();
		Pret pret1 = new Pret(1L, new Date(), new Date(), "remis", 1, livre, utilisateur);
		Pret pret2 = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);

		list.add(pret1);
		list.add(pret1);

		Mockito.when(utilisateurRepository.findByMail(Mockito.anyString())).thenReturn(Optional.of(utilisateur));
		Mockito.when(livreRepository.findByCodeLivre(Mockito.anyLong())).thenReturn(Optional.of(livre));
		Mockito.when(pretRepository.findByUtilisateurAndLivre(Mockito.any(Utilisateur.class), Mockito.any(Livre.class)))
				.thenReturn(list);
		Mockito.when(pretRepository.saveAndFlush(Mockito.any(Pret.class))).thenReturn(pret);
		Mockito.when(pretRepository.findLivreandStatutNotByOrderByDateDeFinAsc(Mockito.any(Livre.class),
				Mockito.anyString())).thenReturn(list);

		pretService.creerPret(1L, "bob@laposte.com");

	}

	@Test
	public void creerUnPretLivreNotFound()
			throws ResultNotFoundException, LivreIndisponibleException, EntityAlreadyExistException {

		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);
		List<Pret> list = new ArrayList<Pret>();
		Pret pret1 = new Pret(1L, new Date(), new Date(), "remis", 1, livre, utilisateur);
		Pret pret2 = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);

		list.add(pret1);
		list.add(pret1);

		Mockito.when(utilisateurRepository.findByMail(Mockito.anyString())).thenReturn(Optional.of(utilisateur));
		Mockito.when(livreRepository.findByCodeLivre(Mockito.anyLong())).thenReturn(Optional.empty());
		Mockito.when(pretRepository.findByUtilisateurAndLivre(Mockito.any(Utilisateur.class), Mockito.any(Livre.class)))
				.thenReturn(list);
		Mockito.when(pretRepository.saveAndFlush(Mockito.any(Pret.class))).thenReturn(pret);
		Mockito.when(pretRepository.findLivreandStatutNotByOrderByDateDeFinAsc(Mockito.any(Livre.class),
				Mockito.anyString())).thenReturn(list);

		ResultNotFoundException exception = assertThrows(ResultNotFoundException.class, () -> {
			pretService.creerPret(1L, "bob@laposte.com");

		});

		String expectedMessage = "le livre n'existe pas";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void creerUnPretUserNotFound()
			throws ResultNotFoundException, LivreIndisponibleException, EntityAlreadyExistException {

		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);
		List<Pret> list = new ArrayList<Pret>();
		Pret pret1 = new Pret(1L, new Date(), new Date(), "remis", 1, livre, utilisateur);
		Pret pret2 = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);

		list.add(pret1);
		list.add(pret1);

		Mockito.when(utilisateurRepository.findByMail(Mockito.anyString())).thenReturn(Optional.empty());
		Mockito.when(livreRepository.findByCodeLivre(Mockito.anyLong())).thenReturn(Optional.of(livre));
		Mockito.when(pretRepository.findByUtilisateurAndLivre(Mockito.any(Utilisateur.class), Mockito.any(Livre.class)))
				.thenReturn(list);
		Mockito.when(pretRepository.saveAndFlush(Mockito.any(Pret.class))).thenReturn(pret);
		Mockito.when(pretRepository.findLivreandStatutNotByOrderByDateDeFinAsc(Mockito.any(Livre.class),
				Mockito.anyString())).thenReturn(list);

		ResultNotFoundException exception = assertThrows(ResultNotFoundException.class, () -> {
			pretService.creerPret(1L, "bob@laposte.com");

		});

		String expectedMessage = "l'utilisateur n'existe pas";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void creerUnPretAlreadyExist()
			throws ResultNotFoundException, LivreIndisponibleException, EntityAlreadyExistException {

		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);
		List<Pret> list = new ArrayList<Pret>();
		Pret pret1 = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);
		Pret pret2 = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);

		list.add(pret1);
		list.add(pret1);

		Mockito.when(utilisateurRepository.findByMail(Mockito.anyString())).thenReturn(Optional.of(utilisateur));
		Mockito.when(livreRepository.findByCodeLivre(Mockito.anyLong())).thenReturn(Optional.of(livre));
		Mockito.when(pretRepository.findByUtilisateurAndLivre(Mockito.any(Utilisateur.class), Mockito.any(Livre.class)))
				.thenReturn(list);
		Mockito.when(pretRepository.saveAndFlush(Mockito.any(Pret.class))).thenReturn(pret);
		Mockito.when(pretRepository.findLivreandStatutNotByOrderByDateDeFinAsc(Mockito.any(Livre.class),
				Mockito.anyString())).thenReturn(list);

		EntityAlreadyExistException exception = assertThrows(EntityAlreadyExistException.class, () -> {
			pretService.creerPret(1L, "bob@laposte.com");

		});

		String expectedMessage = "La reservation existe deja pour ce livre";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void creerUnPretAlreadyExistStockLivreNullEtPretEnattenteExist()
			throws ResultNotFoundException, LivreIndisponibleException, EntityAlreadyExistException {

		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 0, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);
		List<Pret> list = new ArrayList<Pret>();
		Pret pret1 = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);
		Pret pret2 = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);

		list.add(pret1);
		list.add(pret1);

		Mockito.when(utilisateurRepository.findByMail(Mockito.anyString())).thenReturn(Optional.of(utilisateur));
		Mockito.when(livreRepository.findByCodeLivre(Mockito.anyLong())).thenReturn(Optional.of(livre));
		Mockito.when(pretRepository.findByUtilisateurAndLivre(Mockito.any(Utilisateur.class), Mockito.any(Livre.class)))
				.thenReturn(list);
		Mockito.when(pretRepository.saveAndFlush(Mockito.any(Pret.class))).thenReturn(pret);
		Mockito.when(pretRepository.findLivreandStatutNotByOrderByDateDeFinAsc(Mockito.any(Livre.class),
				Mockito.anyString())).thenReturn(list);

		EntityAlreadyExistException exception = assertThrows(EntityAlreadyExistException.class, () -> {
			pretService.creerPret(1L, "bob@laposte.com");

		});

		String expectedMessage = "La reservation existe deja pour ce livre";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void creerUnPretEnattente()
			throws ResultNotFoundException, LivreIndisponibleException, EntityAlreadyExistException, BadException {

		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 5, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);
		List<Pret> list = new ArrayList<Pret>();
		Pret pret1 = new Pret(1L, new Date(), new Date(), "remis", 1, livre, utilisateur);
		Pret pret2 = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);

		list.add(pret1);

		Mockito.when(utilisateurRepository.findByMail(Mockito.anyString())).thenReturn(Optional.of(utilisateur));
		Mockito.when(livreRepository.findByCodeLivre(Mockito.anyLong())).thenReturn(Optional.of(livre));
		Mockito.when(pretRepository.findByUtilisateurAndLivre(Mockito.any(Utilisateur.class), Mockito.any(Livre.class)))
				.thenReturn(list);
		Mockito.when(pretRepository.saveAndFlush(Mockito.any(Pret.class))).thenReturn(pret);
		Mockito.when(pretRepository.findLivreandStatutNotByOrderByDateDeFinAsc(Mockito.any(Livre.class),
				Mockito.anyString())).thenReturn(list);

		pretService.creerPret(1L, "bob@laposte.com");

	}

	@Test
	public void creerUnPretModifierPretattente()
			throws ResultNotFoundException, LivreIndisponibleException, EntityAlreadyExistException, BadException {

		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);
		List<Pret> list = new ArrayList<Pret>();
		Pret pret1 = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);
		Pret pret2 = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);

		list.add(pret1);
		list.add(pret1);

		Mockito.when(utilisateurRepository.findByMail(Mockito.anyString())).thenReturn(Optional.of(utilisateur));
		Mockito.when(livreRepository.findByCodeLivre(Mockito.anyLong())).thenReturn(Optional.of(livre));
		Mockito.when(pretRepository.findByUtilisateurAndLivre(Mockito.any(Utilisateur.class), Mockito.any(Livre.class)))
				.thenReturn(list);
		Mockito.when(pretRepository.saveAndFlush(Mockito.any(Pret.class))).thenReturn(pret);
		Mockito.when(pretRepository.findLivreandStatutNotByOrderByDateDeFinAsc(Mockito.any(Livre.class),
				Mockito.anyString())).thenReturn(list);

		pretService.creerPret(1L, "bob@laposte.com");

	}

	@Test
	public void creerUnPret1()
			throws ResultNotFoundException, LivreIndisponibleException, EntityAlreadyExistException, BadException {

		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		Utilisateur utilisateur1 = new Utilisateur("bob", "bob@gmail.com", "40 rue du chêne", "bob", "2222222", user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);
		List<Pret> list = new ArrayList<Pret>();
		Pret pret1 = new Pret(1L, new Date(), new Date(), "remis", 1, livre, utilisateur1);
		Pret pret2 = new Pret(1L, new Date(), new Date(), "remis", 1, livre, utilisateur);

		list.add(pret1);
		list.add(pret2);

		Mockito.when(utilisateurRepository.findByMail(Mockito.anyString())).thenReturn(Optional.of(utilisateur));
		Mockito.when(livreRepository.findByCodeLivre(Mockito.anyLong())).thenReturn(Optional.of(livre));
		Mockito.when(pretRepository.findByUtilisateurAndLivre(Mockito.any(Utilisateur.class), Mockito.any(Livre.class)))
				.thenReturn(list);
		Mockito.when(pretRepository.saveAndFlush(Mockito.any(Pret.class))).thenReturn(pret);
		Mockito.when(pretRepository.findLivreandStatutNotByOrderByDateDeFinAsc(Mockito.any(Livre.class),
				Mockito.anyString())).thenReturn(list);

		pretService.creerPret(1L, "bob@laposte.com");

	}

	@Test
	public void supprimerUnPret() throws ResultNotFoundException, BadException {
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		PretServiceImpl pretservice1 = Mockito.mock(PretServiceImpl.class);
		Utilisateur utilisateur1 = new Utilisateur("bob", "bob@gmail.com", "40 rue du chêne", "bob", "2222222", user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);

		Mockito.when(livreRepository.findById(Mockito.any())).thenReturn(Optional.of(livre));
		Mockito.when(pretRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(pret));

		Mockito.when(utilisateurRepository.findByMail(Mockito.any())).thenReturn(Optional.of(utilisateur));
		Mockito.when(pretRepository.findByUtilisateurAndLivreAndStatut(Mockito.any(Utilisateur.class),
				Mockito.any(Livre.class), Mockito.anyString())).thenReturn(Optional.of(pret));

		Mockito.when(pretRepository.saveAndFlush(pret)).thenReturn(pret);
		Mockito.when(livreRepository.saveAndFlush(livre)).thenReturn(livre);

		Mockito.doNothing().when(pretRepository).delete(pret);

		pretService.supprimerPret(1L, Optional.of("enattente"));

	}

	@Test
	public void supprimerUnPret1() throws ResultNotFoundException, BadException {
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		PretServiceImpl pretservice1 = Mockito.mock(PretServiceImpl.class);
		Utilisateur utilisateur1 = new Utilisateur("bob", "bob@gmail.com", "40 rue du chêne", "bob", "2222222", user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);

		Mockito.when(livreRepository.findById(Mockito.any())).thenReturn(Optional.of(livre));
		Mockito.when(pretRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(pret));

		Mockito.when(utilisateurRepository.findByMail(Mockito.any())).thenReturn(Optional.of(utilisateur));
		Mockito.when(pretRepository.findByUtilisateurAndLivreAndStatut(Mockito.any(Utilisateur.class),
				Mockito.any(Livre.class), Mockito.anyString())).thenReturn(Optional.of(pret));

		Mockito.when(pretRepository.saveAndFlush(pret)).thenReturn(pret);
		Mockito.when(livreRepository.saveAndFlush(livre)).thenReturn(livre);

		Mockito.doNothing().when(pretRepository).delete(pret);

		pretService.supprimerPret(1L, Optional.of("encours1"));

	}

	@Test
	public void supprimerUnPretExceptionNotFound() throws ResultNotFoundException, BadException {
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		PretServiceImpl pretservice1 = Mockito.mock(PretServiceImpl.class);
		Utilisateur utilisateur1 = new Utilisateur("bob", "bob@gmail.com", "40 rue du chêne", "bob", "2222222", user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);

		Mockito.when(livreRepository.findById(Mockito.any())).thenReturn(Optional.of(livre));
		Mockito.when(pretRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		ResultNotFoundException exception = assertThrows(ResultNotFoundException.class, () -> {
			pretService.supprimerPret(1L, Optional.of("enattente"));

		});

		String expectedMessage = "ce pret n'existe pas";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void supprimerUnPretExceptionNotFound1() throws ResultNotFoundException, BadException {
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		PretServiceImpl pretservice1 = Mockito.mock(PretServiceImpl.class);
		Utilisateur utilisateur1 = new Utilisateur("bob", "bob@gmail.com", "40 rue du chêne", "bob", "2222222", user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);

		Mockito.when(livreRepository.findById(Mockito.any())).thenReturn(Optional.of(livre));
		Mockito.when(pretRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		ResultNotFoundException exception = assertThrows(ResultNotFoundException.class, () -> {
			pretService.supprimerPret(1L, Optional.of("encours1"));

		});

		String expectedMessage = "ce pret n'existe pas";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void supprimerUnPretBadException() throws ResultNotFoundException, BadException {
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		PretServiceImpl pretservice1 = Mockito.mock(PretServiceImpl.class);
		Utilisateur utilisateur1 = new Utilisateur("bob", "bob@gmail.com", "40 rue du chêne", "bob", "2222222", user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);

		Mockito.when(livreRepository.findById(Mockito.any())).thenReturn(Optional.of(livre));
		Mockito.when(pretRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(pret));

		BadException exception = assertThrows(BadException.class, () -> {
			pretService.supprimerPret(1L, Optional.of("encour1"));

		});

		String expectedMessage = "le pret ne peut pas etre supprimer";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void modifierPret() throws ResultNotFoundException, EntityAlreadyExistException {
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		Utilisateur utilisateur1 = new Utilisateur("bob", "bob@gmail.com", "40 rue du chêne", "bob", "2222222", user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);

		Mockito.when(pretRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(pret));
		Mockito.when(livreRepository.findByCodeLivre(Mockito.any())).thenReturn(Optional.of(livre));
		Mockito.when(pretRepository.saveAndFlush(pret)).thenReturn(pret);
		Mockito.when(livreRepository.saveAndFlush(livre)).thenReturn(livre);
		Mockito.when(livreRepository.findById(Mockito.any())).thenReturn(Optional.of(livre));
		Mockito.when(utilisateurRepository.findByMail(Mockito.any())).thenReturn(Optional.of(utilisateur));
		Mockito.when(pretRepository.findByUtilisateurAndLivreAndStatut(Mockito.any(Utilisateur.class),
				Mockito.any(Livre.class), Mockito.anyString())).thenReturn(Optional.of(pret));

		pretService.modifierPret(1L, "remise");

	}

	@Test
	public void modifierPretEntityAlreadyExistException() throws ResultNotFoundException, EntityAlreadyExistException {
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		Utilisateur utilisateur1 = new Utilisateur("bob", "bob@gmail.com", "40 rue du chêne", "bob", "2222222", user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "remis", 1, livre, utilisateur);

		Mockito.when(pretRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(pret));
		Mockito.when(livreRepository.findByCodeLivre(Mockito.any())).thenReturn(Optional.of(livre));

		EntityAlreadyExistException exception = assertThrows(EntityAlreadyExistException.class, () -> {
			pretService.modifierPret(1L, "remise");
		});

		String expectedMessage = "Le pret ne peut pas etre prolongé";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void modifierPret1() throws ResultNotFoundException, EntityAlreadyExistException {
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		Utilisateur utilisateur1 = new Utilisateur("bob", "bob@gmail.com", "40 rue du chêne", "bob", "2222222", user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 0, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);
		List<Pret> list = new ArrayList<Pret>();
		Pret pret1 = new Pret(1L, new Date(), new Date(), "remis", 1, livre, utilisateur);
		Pret pret2 = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);

		list.add(pret1);
		list.add(pret2);

		Mockito.when(pretRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(pret));
		Mockito.when(livreRepository.findByCodeLivre(Mockito.any())).thenReturn(Optional.of(livre));
		Mockito.when(pretRepository.saveAndFlush(pret)).thenReturn(pret);
		Mockito.when(livreRepository.saveAndFlush(livre)).thenReturn(livre);
		Mockito.when(livreRepository.findById(Mockito.any())).thenReturn(Optional.of(livre));
		Mockito.when(utilisateurRepository.findByMail(Mockito.any())).thenReturn(Optional.of(utilisateur));
		Mockito.when(pretRepository.findByUtilisateurAndLivreAndStatut(Mockito.any(Utilisateur.class),
				Mockito.any(Livre.class), Mockito.anyString())).thenReturn(Optional.of(pret));
		Mockito.when(pretRepository.findLivreandStatutNotByOrderByDateDeFinAsc(Mockito.any(Livre.class),
				Mockito.anyString())).thenReturn(list);

		pretService.modifierPret(1L, "");
	}

	@Test
	public void modifierPretExceptionEntytiAlreadyException()
			throws ResultNotFoundException, EntityAlreadyExistException {
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		Utilisateur utilisateur1 = new Utilisateur("bob", "bob@gmail.com", "40 rue du chêne", "bob", "2222222", user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 0, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);
		List<Pret> list = new ArrayList<Pret>();
		Pret pret1 = new Pret(1L, new Date(), new Date(), "remis", 1, livre, utilisateur);
		Pret pret2 = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);

		list.add(pret1);
		list.add(pret2);

		Mockito.when(pretRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(pret));
		Mockito.when(livreRepository.findByCodeLivre(Mockito.any())).thenReturn(Optional.of(livre));
		Mockito.when(pretRepository.saveAndFlush(pret)).thenReturn(pret);
		Mockito.when(livreRepository.saveAndFlush(livre)).thenReturn(livre);
		Mockito.when(livreRepository.findById(Mockito.any())).thenReturn(Optional.of(livre));
		Mockito.when(utilisateurRepository.findByMail(Mockito.any())).thenReturn(Optional.of(utilisateur));
		Mockito.when(pretRepository.findByUtilisateurAndLivreAndStatut(Mockito.any(Utilisateur.class),
				Mockito.any(Livre.class), Mockito.anyString())).thenReturn(Optional.of(pret));
		Mockito.when(pretRepository.findLivreandStatutNotByOrderByDateDeFinAsc(Mockito.any(Livre.class),
				Mockito.anyString())).thenReturn(list);

		EntityAlreadyExistException exception = assertThrows(EntityAlreadyExistException.class, () -> {
			pretService.modifierPret(1L, "");
		});

		String expectedMessage = "Ce pret ne peu pas etre modifier par le méthode modifier pret";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void modifierPretPretNotFoundException() throws ResultNotFoundException, EntityAlreadyExistException {
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		Utilisateur utilisateur1 = new Utilisateur("bob", "bob@gmail.com", "40 rue du chêne", "bob", "2222222", user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);

		Mockito.when(pretRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		ResultNotFoundException exception = assertThrows(ResultNotFoundException.class, () -> {
			pretService.modifierPret(1L, "remise");
		});

		String expectedMessage = "Ce pret n'existe pas";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void modifierPretLivreNotFoundException() throws ResultNotFoundException, EntityAlreadyExistException {
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		Utilisateur utilisateur1 = new Utilisateur("bob", "bob@gmail.com", "40 rue du chêne", "bob", "2222222", user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);

		Mockito.when(pretRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(pret));
		Mockito.when(livreRepository.findByCodeLivre(Mockito.any())).thenReturn(Optional.empty());

		ResultNotFoundException exception = assertThrows(ResultNotFoundException.class, () -> {
			pretService.modifierPret(1L, "remise");
		});

		String expectedMessage = "Ce livre n'existe pas";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void modifierLesPositionsDesPretsEnListeDattentes() throws ResultNotFoundException {

		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		List<String> listmails = new ArrayList<String>();
		listmails.add("gualisse@gmail.com");
		listmails.add("bob@laposte.com");

		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, listmails);
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);

		Mockito.when(livreRepository.findById(Mockito.any())).thenReturn(Optional.of(livre));
		Mockito.when(pretRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(pret));

		Mockito.when(utilisateurRepository.findByMail(Mockito.any())).thenReturn(Optional.of(utilisateur));
		Mockito.when(pretRepository.findByUtilisateurAndLivreAndStatut(Mockito.any(Utilisateur.class),
				Mockito.any(Livre.class), Mockito.anyString())).thenReturn(Optional.of(pret));

		Mockito.when(pretRepository.saveAndFlush(pret)).thenReturn(pret);
		Mockito.when(livreRepository.saveAndFlush(livre)).thenReturn(livre);

		pretService.modifierLesPositionsDesPretsEnListeDattentes(1L, 1);

	}

	@Test
	public void modifierLesPositionsDesPretsEnListeDattentesUtilisateurIntrouvable() throws ResultNotFoundException {

		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		List<String> listmails = new ArrayList<String>();
		listmails.add("gualisse@gmail.com");
		listmails.add("bob@laposte.com");

		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, listmails);
		livre.setNombreListeDattente(2);
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);

		Mockito.when(livreRepository.findById(Mockito.any())).thenReturn(Optional.of(livre));
		Mockito.when(pretRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(pret));

		Mockito.when(utilisateurRepository.findByMail(Mockito.any())).thenReturn(Optional.empty());

		ResultNotFoundException exception = assertThrows(ResultNotFoundException.class, () -> {
			pretService.modifierLesPositionsDesPretsEnListeDattentes(1L, 1);

		});

		String expectedMessage = "l'utilisateur est introuvable";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void modifierLesPositionsDesPretsEnListeDattenteslivreIntrouvable() throws ResultNotFoundException {

		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		List<String> listmails = new ArrayList<String>();
		listmails.add("gualisse@gmail.com");
		listmails.add("bob@laposte.com");

		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, listmails);
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);

		Mockito.when(livreRepository.findById(Mockito.any())).thenReturn(Optional.empty());
		Mockito.when(pretRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(pret));

		Mockito.when(utilisateurRepository.findByMail(Mockito.any())).thenReturn(Optional.of(utilisateur));
		Mockito.when(pretRepository.findByUtilisateurAndLivreAndStatut(Mockito.any(Utilisateur.class),
				Mockito.any(Livre.class), Mockito.anyString())).thenReturn(Optional.of(pret));

		Mockito.when(pretRepository.saveAndFlush(pret)).thenReturn(pret);
		Mockito.when(livreRepository.saveAndFlush(livre)).thenReturn(livre);

		ResultNotFoundException exception = assertThrows(ResultNotFoundException.class, () -> {
			pretService.modifierLesPositionsDesPretsEnListeDattentes(1L, 1);

		});

		String expectedMessage = "le livre est introuvable";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	public void connectApiTimer() {

		HttpService httpServices = Mockito.mock(HttpServiceImpl.class);
		HttpHeaders headers = httpService.creerHeadersHttpAuthentifie("gualiss@gmail.com", "gualisse");
		ResponseEntity<Long> responseEntity = new ResponseEntity<Long>(1L, HttpStatus.OK);
		final String uri = "http://localhost:8085/timer";
		Mockito.when(httpServices.creerHeadersHttpAuthentifie(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(headers);

		Mockito.when(restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(1L, headers), Long.class))
				.thenReturn(responseEntity);

		connectionApiService.connectApiTimer(1L);
	}

	@Test
	public void verifierPret() throws ResultNotFoundException, BadException, EntityAlreadyExistException {

		PretServiceImpl pretServices = Mockito.mock(PretServiceImpl.class);
		EmailServiceImpl emailServices = Mockito.mock(EmailServiceImpl.class);

		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		List<String> listmails = new ArrayList<String>();
		listmails.add("gualisse@gmail.com");
		listmails.add("bob@laposte.com");
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, listmails);
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);

		List<Pret> listPrets = new ArrayList<Pret>();
		Pret pret1 = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);
		Pret pret2 = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);
		listPrets.add(pret1);
		listPrets.add(pret2);

		Mockito.when(pretRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(pret));
		Mockito.when(livreRepository.findByNom(Mockito.anyString())).thenReturn(Optional.of(livre));
		Mockito.when(livreRepository.saveAndFlush(Mockito.any(Livre.class))).thenReturn(livre);

		Mockito.doNothing().when(pretServices).supprimerPret(Mockito.anyLong(), Optional.of(Mockito.anyString()));
		Mockito.when(livreRepository.saveAndFlush(Mockito.any(Livre.class))).thenReturn(livre);

		Mockito.doNothing().when(pretRepository).delete(pret);
		Mockito.when(emailServices.createHtmlContent(Mockito.anyString(), Mockito.any(Livre.class),
				Mockito.any(Locale.class))).thenReturn("bob");
		Mockito.doNothing().when(emailServices).sendMail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.any(Locale.class));
		Mockito.when(pretRepository.findByStatutAndLivre(Mockito.anyString(), Mockito.any(Livre.class)))
				.thenReturn(listPrets);

		Mockito.doNothing().when(connectionApiService).connectApiTimer(1L);

		pretService.verifierPrêt(1L);

	}

	@Test
	public void verifierPret1() throws ResultNotFoundException, BadException, EntityAlreadyExistException {

		PretServiceImpl pretServices = Mockito.mock(PretServiceImpl.class);
		EmailServiceImpl emailServices = Mockito.mock(EmailServiceImpl.class);

		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		List<String> listmails = new ArrayList<String>();
		listmails.add("gualisse@gmail.com");
		listmails.add("bob@laposte.com");
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, listmails);
		Pret pret = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);

		List<Pret> listPrets = new ArrayList<Pret>();
		Pret pret1 = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);
		Pret pret2 = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);
		listPrets.add(pret1);
		listPrets.add(pret2);

		Mockito.when(pretRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(pret));
		Mockito.when(livreRepository.findByNom(Mockito.anyString())).thenReturn(Optional.of(livre));
		Mockito.when(livreRepository.saveAndFlush(Mockito.any(Livre.class))).thenReturn(livre);

		Mockito.doNothing().when(pretServices).supprimerPret(Mockito.anyLong(), Optional.of(Mockito.anyString()));

		Mockito.when(emailServices.createHtmlContent(Mockito.anyString(), Mockito.any(Livre.class),
				Mockito.any(Locale.class))).thenReturn("bob");
		Mockito.doNothing().when(emailServices).sendMail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.any(Locale.class));
		Mockito.when(pretRepository.findByStatutAndLivre(Mockito.anyString(), Mockito.any(Livre.class)))
				.thenReturn(listPrets);
		Mockito.when(livreRepository.findById(Mockito.any())).thenReturn(Optional.of(livre));
		Mockito.when(pretRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(pret));

		Mockito.when(utilisateurRepository.findByMail(Mockito.any())).thenReturn(Optional.of(utilisateur));
		Mockito.when(pretRepository.findByUtilisateurAndLivreAndStatut(Mockito.any(Utilisateur.class),
				Mockito.any(Livre.class), Mockito.anyString())).thenReturn(Optional.of(pret));

		Mockito.when(pretRepository.saveAndFlush(pret)).thenReturn(pret);
		Mockito.when(livreRepository.saveAndFlush(livre)).thenReturn(livre);

		Mockito.doNothing().when(pretRepository).delete(pret);

		Mockito.doNothing().when(connectionApiService).connectApiTimer(1L);

		pretService.verifierPrêt(1L);

	}

	@Test
	public void verifierPretPretNotFoundException()
			throws ResultNotFoundException, BadException, EntityAlreadyExistException {

		Mockito.when(pretRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		ResultNotFoundException exception = assertThrows(ResultNotFoundException.class, () -> {
			pretService.verifierPrêt(1L);

		});

		String expectedMessage = "le pret n'existe pas";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void verifierPretLivreNotFound() throws ResultNotFoundException, BadException, EntityAlreadyExistException {

		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		List<String> listmails = new ArrayList<String>();
		listmails.add("gualisse@gmail.com");
		listmails.add("bob@laposte.com");
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, listmails);
		Pret pret = new Pret(1L, new Date(), new Date(), "encours", 1, livre, utilisateur);

		Mockito.when(pretRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(pret));
		Mockito.when(livreRepository.findByNom(Mockito.anyString())).thenReturn(Optional.empty());

		ResultNotFoundException exception = assertThrows(ResultNotFoundException.class, () -> {
			pretService.verifierPrêt(1L);

		});

		String expectedMessage = "le livre n'existe pas";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

	}

}
