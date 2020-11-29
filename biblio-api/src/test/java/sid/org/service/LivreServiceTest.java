package sid.org.service;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
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
import sid.org.dao.LivreRepository;
import sid.org.dto.LivreDto;
import sid.org.exception.BadException;
import sid.org.exception.EntityAlreadyExistException;
import sid.org.exception.ResultNotFoundException;
import sid.org.specification.LivreCriteria;
import sid.org.specification.LivreSpecificationImpl;

@RunWith(SpringRunner.class)
@SpringBootTest

@ContextConfiguration
public class LivreServiceTest {

	@MockBean
	LivreRepository livreRepository;
	@Autowired
	LivreService livreService;

	@Test
	public void createLivre() throws EntityAlreadyExistException, BadException {
		List<Livre> listLivre = new ArrayList<Livre>();
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		LivreDto livreDto = new LivreDto("les comptes", "guiz", "type1", "section1", "emplacement", 1);

		Mockito.when(livreRepository.findByAuteurAndNom(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(listLivre);
		Mockito.when(livreRepository.save(Mockito.any(Livre.class))).thenReturn(livre);

		Livre livre1 = livreService.createLivre(livreDto);

		assertEquals(livre1, livre);

	}

	@Test
	public void createLivreExceptionLivreExist() throws EntityAlreadyExistException {
		List<Livre> listLivre = new ArrayList<Livre>();
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		LivreDto livreDto = new LivreDto("les comptes", "guiz", "type1", "section1", "emplacement", 1);
		listLivre.add(livre);
		Mockito.when(livreRepository.findByAuteurAndNom(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(listLivre);
		Mockito.when(livreRepository.save(Mockito.any(Livre.class))).thenReturn(livre);

		EntityAlreadyExistException exception = assertThrows(EntityAlreadyExistException.class, () -> {
			Livre livre1 = livreService.createLivre(livreDto);
		});

		String expectedMessage = "Ce livre existe deja";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void supprimerUnLivre() throws ResultNotFoundException {
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());

		LivreService livreService = Mockito.mock(LivreService.class);

		Mockito.when(livreRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(livre));
		Mockito.doNothing().when(livreRepository).delete(livre);

		livreService.supprimerLivre(1L);

	}

	@Test
	public void supprimerUnLivreExceptionLivreNotFound() throws ResultNotFoundException {
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());

		LivreService livreService = Mockito.mock(LivreService.class);

		Mockito.when(livreRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		Mockito.doNothing().when(livreRepository).delete(livre);

		ResultNotFoundException exception = assertThrows(ResultNotFoundException.class, () -> {
			livreService.supprimerLivre(1L);
		});

		String expectedMessage = "le livre existe pas";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void afficherUnLivre() throws ResultNotFoundException {
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());

		Mockito.when(livreRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(livre));

		Livre livre1 = livreService.afficheUnLivre(1L);

		assertEquals(livre.getAuteur(), livre.getAuteur());
		assertEquals(livre.getNom(), livre.getNom());
	}

	@Test
	public void afficherUnLivreExceptionNotFound() throws ResultNotFoundException {
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());

		Mockito.when(livreRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		ResultNotFoundException exception = assertThrows(ResultNotFoundException.class, () -> {
			Livre livre1 = livreService.afficheUnLivre(1L);
		});

		String expectedMessage = "le livre existe pas";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void searchLivres() throws ResultNotFoundException {
		LivreCriteria livrecriteria = new LivreCriteria(1L, "le", "", "", "", "", 1);
		LivreSpecificationImpl spec = Mockito.mock(LivreSpecificationImpl.class);
		int page = 1;
		int size = 3;
		Pageable pageable = Mockito.mock(Pageable.class);
		List<Livre> listLivre = new ArrayList<Livre>();
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		listLivre.add(livre);
		Page<Livre> listlivres = new PageImpl<Livre>(listLivre);

		Mockito.when(livreRepository.findAll(Mockito.any(LivreSpecificationImpl.class), Mockito.any(Pageable.class)))
				.thenReturn(listlivres);

		Page<Livre> page1 = livreService.searchLivres(livrecriteria, page, size);

		assertEquals(page1.getSize(), 1l);

	}

	@Test
	public void searchLivresSizePageNull() throws ResultNotFoundException {
		LivreCriteria livrecriteria = new LivreCriteria(1L, "le", "", "", "", "", 1);
		LivreSpecificationImpl spec = Mockito.mock(LivreSpecificationImpl.class);
		int page = 1;
		int size = 0;
		Pageable pageable = Mockito.mock(Pageable.class);
		List<Livre> listLivre = new ArrayList<Livre>();
		Livre livre = new Livre("les comptes", "guiz", "type1", "section1", "emplacement", 1, new ArrayList<String>());
		listLivre.add(livre);
		Page<Livre> listlivres = new PageImpl<Livre>(listLivre);

		Mockito.when(livreRepository.findAll(Mockito.any(LivreSpecificationImpl.class), Mockito.any(Pageable.class)))
				.thenReturn(listlivres);

		ResultNotFoundException exception = assertThrows(ResultNotFoundException.class, () -> {
			Page<Livre> page1 = livreService.searchLivres(livrecriteria, page, size);

		});

		String expectedMessage = "le parametre size est incorrect";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));

	}

}