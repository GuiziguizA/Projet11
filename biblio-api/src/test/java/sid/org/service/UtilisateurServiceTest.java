package sid.org.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import sid.org.classe.Livre;
import sid.org.classe.Pret;
import sid.org.classe.Roles;
import sid.org.classe.Sessions;
import sid.org.classe.Utilisateur;
import sid.org.dao.LivreRepository;
import sid.org.dao.PretRepository;
import sid.org.dao.RolesRepository;
import sid.org.dao.UtilisateurRepository;
import sid.org.dto.UtilisateurDto;
import sid.org.exception.BadException;
import sid.org.exception.EntityAlreadyExistException;
import sid.org.exception.MotDePasseInvalidException;
import sid.org.exception.ResultNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest

@ContextConfiguration

public class UtilisateurServiceTest {

	@Autowired
	private UtilisateurService utilisateurService;
	@MockBean
	private UtilisateurRepository utilisateurRepository;
	@MockBean
	private PretRepository pretRepository;
	@MockBean
	private RolesRepository rolesRepository;
	@MockBean
	private LivreRepository livreRepository;
	@MockBean
	PasswordEncoder passwordEncoder;

	@BeforeEach
	void setMockOutput() {

	}

	@Test
	public void voirUnUtilisateur() throws ResultNotFoundException {
		Roles user = new Roles("user");
		Utilisateur alex = new Utilisateur("Marcel", "marcel@gmail.com", "14 avenue du chene", "marcel", "65123", user);
		Mockito.when(utilisateurRepository.findByMail("marcel@gmail.com")).thenReturn(Optional.of(alex));
		Utilisateur utilisateur = utilisateurService.voirUtilisateur("marcel@gmail.com");
		assertEquals(utilisateur.getMail(), "marcel@gmail.com");
		assertEquals(utilisateur.getNom(), "Marcel");

	}

	@Test
	public void voirUnUtilisateurResultNotFoundException() throws ResultNotFoundException {
		Mockito.when(utilisateurRepository.findByMail("bob@laposte.com")).thenReturn(Optional.empty());
		ResultNotFoundException exception = assertThrows(ResultNotFoundException.class, () -> {
			Utilisateur utilisateur = utilisateurService.voirUtilisateur("bob@laposte.com");
		});

		String expectedMessage = "Utilisateur introuvable";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void creerUnUtilisateur() throws EntityAlreadyExistException, BadException {
		// GIVEN
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "22222", user);

		Mockito.when(utilisateurRepository.findByMail("bob@laposte.com")).thenReturn(Optional.empty());
		Mockito.when(passwordEncoder.encode("bob")).thenReturn("bob");
		Mockito.when(utilisateurRepository.save(Mockito.any(Utilisateur.class))).thenReturn(utilisateur);
		Mockito.when(rolesRepository.findByNom(Mockito.any(String.class))).thenReturn(Optional.of(user));

		// WHEN
		UtilisateurDto userDto = new UtilisateurDto("emile", "bob@laposte.com", "40 rue du chêne", "bob", "22222");
		Utilisateur user1 = utilisateurService.creerUtilisateur(userDto, "utilisateur");

		// THEN
		assertEquals(user1.getMail(), "bob@laposte.com");
		assertEquals(user1.getMotDePasse(), "bob");
	}

	@Test
	public void creerUnUtilisateurUtilisateurException() throws EntityAlreadyExistException, BadException {
		// GIVEN
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "22222", user);

		Mockito.when(utilisateurRepository.findByMail("bob@laposte.com")).thenReturn(Optional.of(utilisateur));
		Mockito.when(passwordEncoder.encode("bob")).thenReturn("bob");
		Mockito.when(utilisateurRepository.save(Mockito.any(Utilisateur.class))).thenReturn(utilisateur);
		Mockito.when(rolesRepository.findByNom(Mockito.any(String.class))).thenReturn(Optional.of(user));

		// WHEN
		UtilisateurDto userDto = new UtilisateurDto("emile", "bob@laposte.com", "40 rue du chêne", "bob", "22222");
		EntityAlreadyExistException exception = assertThrows(EntityAlreadyExistException.class, () -> {
			Utilisateur user1 = utilisateurService.creerUtilisateur(userDto, "utilisateur");
		});

		String expectedMessage = "le mail est deja utilise";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void creerUnUtilisateurcodePostalException() throws EntityAlreadyExistException, BadException {
		// GIVEN
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);

		Mockito.when(utilisateurRepository.findByMail("bob@laposte.com")).thenReturn(Optional.empty());
		Mockito.when(passwordEncoder.encode("bob")).thenReturn("bob");
		Mockito.when(utilisateurRepository.save(Mockito.any(Utilisateur.class))).thenReturn(utilisateur);
		Mockito.when(rolesRepository.findByNom(Mockito.any(String.class))).thenReturn(Optional.of(user));

		// WHEN
		UtilisateurDto userDto = new UtilisateurDto("emile", "bob@laposte.com", "40 rue du chêne", "bob", "222222");
		BadException exception = assertThrows(BadException.class, () -> {
			Utilisateur user1 = utilisateurService.creerUtilisateur(userDto, "utilisateur");
		});

		String expectedMessage = "le code postal doit contenir 5 chiffres";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void modifierUnUtilisateur() throws ResultNotFoundException {
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		Mockito.when(utilisateurRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(utilisateur));
		Mockito.when(utilisateurRepository.save(Mockito.any(Utilisateur.class))).thenReturn(utilisateur);

		Utilisateur utilisateur1 = utilisateurService.modifierUtilisateur(1L, "employe");

		assertEquals(utilisateur1.getRoles().getNom(), "employe");

	}

	@Test
	public void modifierUnUtilisateurUserNotFound() throws ResultNotFoundException {
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		Mockito.when(utilisateurRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
		Mockito.when(utilisateurRepository.save(Mockito.any(Utilisateur.class))).thenReturn(utilisateur);
		ResultNotFoundException exception = assertThrows(ResultNotFoundException.class, () -> {
			utilisateurService.modifierUtilisateur(1L, "employe");
		});

		String expectedMessage = "Utilisateur introuvable";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void supprimerUnUtilisateur() throws ResultNotFoundException {

		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		Livre livre = new Livre();
		Pret pret = new Pret(1L, new Date(), new Date(), "en cours", 1, livre, utilisateur);
		List<Pret> listPret = new ArrayList<Pret>();
		listPret.add(pret);
		Page<Pret> listPrets = new PageImpl<Pret>(listPret);

		Mockito.when(utilisateurRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(utilisateur));
		Mockito.when(pretRepository.findByUtilisateur(Mockito.any(Utilisateur.class), Mockito.any(Pageable.class)))
				.thenReturn(listPrets);
		Mockito.doNothing().when(utilisateurRepository).delete(Mockito.any(Utilisateur.class));

		Mockito.doNothing().when(pretRepository).deleteAll(listPrets);

		utilisateurService.supprimerUtilisateur(1L);

	}

	@Test
	public void voirListeUtilisateur() throws ResultNotFoundException {
		int page = 2;
		int size = 2;
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		Livre livre = new Livre();
		Pret pret = new Pret(1L, new Date(), new Date(), "en cours", 1, livre, utilisateur);
		List<Utilisateur> listUser = new ArrayList<Utilisateur>();
		listUser.add(utilisateur);
		Page<Utilisateur> listUsers = new PageImpl<Utilisateur>(listUser);

		Mockito.when(utilisateurRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(listUsers);

		Page<Utilisateur> listUsers1 = utilisateurService.voirListeUtilisateurs(page, size);

		assertEquals(listUsers1.getSize(), 1);

	}

	@Test
	public void voirListeUtilisateurSize0() throws ResultNotFoundException {
		int page = 2;
		int size = 0;
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@laposte.com", "40 rue du chêne", "bob", "2222222",
				user);
		Livre livre = new Livre();
		Pret pret = new Pret(1L, new Date(), new Date(), "en cours", 1, livre, utilisateur);
		List<Utilisateur> listUser = new ArrayList<Utilisateur>();
		listUser.add(utilisateur);
		Page<Utilisateur> listUsers = new PageImpl<Utilisateur>(listUser);

		Mockito.when(utilisateurRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(listUsers);

		ResultNotFoundException exception = assertThrows(ResultNotFoundException.class, () -> {
			utilisateurService.voirListeUtilisateurs(page, size);
		});

		String expectedMessage = "le parametre size est incorrect";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void connectionUtilisateur() throws ResultNotFoundException, MotDePasseInvalidException {
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@gmail.com", "40 rue du chêne", "bob", "2222222", user);
		Sessions session = new Sessions("bob@gmail.com", "bob");

		boolean debug = true;

		Mockito.when(utilisateurRepository.findByMail("bob@gmail.com")).thenReturn(Optional.of(utilisateur));

		Mockito.when(passwordEncoder.matches(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(debug);

		Optional<Utilisateur> user1 = utilisateurService.connectionUtilisateur(session);

		assertEquals(user1.get().getMail(), "bob@gmail.com");

	}

	@Test
	public void connectionUtilisateurInvalideMotDePasse() throws ResultNotFoundException, MotDePasseInvalidException {
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@gmail.com", "40 rue du chêne", "bob", "2222222", user);
		Sessions session = new Sessions("bob@gmail.com", "bob");
		boolean debug = false;

		Mockito.when(utilisateurRepository.findByMail("bob@gmail.com")).thenReturn(Optional.of(utilisateur));

		Mockito.when(passwordEncoder.matches(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(debug);

		MotDePasseInvalidException exception = assertThrows(MotDePasseInvalidException.class, () -> {

			Optional<Utilisateur> user1 = utilisateurService.connectionUtilisateur(session);
		});

		String expectedMessage = "mot de passe invalide";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));

		/* assertEquals(user1.get().getMail(), "bob@gmail.com"); */

	}

	@Test
	public void connectionUtilisateurUtilisateurIntrouvable()
			throws ResultNotFoundException, MotDePasseInvalidException {
		Roles user = new Roles("user");
		Utilisateur utilisateur = new Utilisateur("emile", "bob@gmail.com", "40 rue du chêne", "bob", "2222222", user);
		Sessions session = new Sessions("bob@gmail.com", "bob");

		Mockito.when(utilisateurRepository.findByMail("bob@gmail.com")).thenReturn(Optional.empty());

		ResultNotFoundException exception = assertThrows(ResultNotFoundException.class, () -> {
			Optional<Utilisateur> user1 = utilisateurService.connectionUtilisateur(session);

		});

		String expectedMessage = "Il n'existe aucun compte contenant cette adresse e-mail";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));

	}

}
