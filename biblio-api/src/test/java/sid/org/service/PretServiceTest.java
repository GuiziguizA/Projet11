package sid.org.service;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

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

		Mockito.when(pretRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(pret));
		Mockito.when(pretRepository.saveAndFlush(Mockito.any(Pret.class))).thenReturn(pret);

		ResultNotFoundException exception = assertThrows(ResultNotFoundException.class, () -> {
			pretService.modifierStatut(1L);

		});

		String expectedMessage = "le pret n'existe pas";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void creerUnPret() throws ResultNotFoundException, LivreIndisponibleException, EntityAlreadyExistException {

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

		String expectedMessage = "le livre n'existe pas";
		String actualMessage = exception.getMessage();

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

	}

	@Test
	public void creerUnPretEnattente()
			throws ResultNotFoundException, LivreIndisponibleException, EntityAlreadyExistException {

		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 0, new ArrayList<String>());
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
	public void creerUnPretModifierPretattente()
			throws ResultNotFoundException, LivreIndisponibleException, EntityAlreadyExistException {

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
	public void creerUnPret1() throws ResultNotFoundException, LivreIndisponibleException, EntityAlreadyExistException {

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
		Utilisateur utilisateur1 = new Utilisateur("bob", "bob@gmail.com", "40 rue du chêne", "bob", "2222222", user);
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		Pret pret = new Pret(1L, new Date(), new Date(), "enattente", 1, livre, utilisateur);

		Mockito.when(pretRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(pret));
		Mockito.when(livreRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(livre));
		Mockito.when(livreRepository.saveAndFlush(livre)).thenReturn(livre);

		Mockito.doThrow(new RuntimeException()).when(pretRepository).delete(pret);
		pretService.supprimerPret(1L, Optional.of("encours1"));

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
		Mockito.when(livreRepository.findByCodeLivre(Mockito.anyLong())).thenReturn(Optional.of(livre));
		Mockito.when(pretRepository.saveAndFlush(pret)).thenReturn(pret);
		Mockito.when(livreRepository.saveAndFlush(livre)).thenReturn(livre);
		EmailService emailservice = Mockito.mock(EmailServiceImpl.class);

		pretService.modifierPret(1L, "remise");

	}

}
