package sid.org.biblio.front.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpStatusCodeException;

import sid.org.biblio.front.classe.Livre;
import sid.org.biblio.front.classe.Utilisateur;
import sid.org.biblio.front.enumeration.Types;
import sid.org.biblio.front.service.BookService;
import sid.org.biblio.front.service.HttpService;
import sid.org.biblio.front.service.UtilisateurService;

@Controller

public class BooksController {
	@Autowired
	private BookService bookService;
	@Autowired
	private UtilisateurService utilisateurService;
	@Autowired
	private HttpService httpService;

	@Secured(value = { "ROLE_admin", "ROLE_employe" })
	@GetMapping(value = "/books/form")
	public String Book(Livre livre,Model model) {

		
		return "formulaireLivre";
	}

	@Secured(value = { "ROLE_admin", "ROLE_employe" })
	@PostMapping("/books")
	public String createBook(@Valid Livre livre, BindingResult result, Model model, HttpServletRequest request) {

		HttpSession session = request.getSession();
		String motDePasse = (String) session.getAttribute("password");
		String mail = (String) session.getAttribute("username");

		Utilisateur user = utilisateurService.infosUtilisateur(mail, motDePasse);
		model.addAttribute("role", user.getRoles().getNom());

		List<Types> listTypes = bookService.chargerLesTypesDeRecherches();
		model.addAttribute("listTypes", listTypes);

		try {
			bookService.createLivre(livre, mail, motDePasse);
			String succes = "Le livre a été ajouté";
			model.addAttribute("succes", succes);
			return "home";
		} catch (HttpStatusCodeException e) {
			String error = httpService.traiterLesExceptionsApi(e);
			model.addAttribute("error", error);
			return "formulaireLivre";
		}

	}

	@GetMapping(value = "/books")
	public String listBooks(Model model, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(required = false) Optional<Integer> size,
			@RequestParam(required = false) Optional<String> type,
			@RequestParam(required = false) Optional<String> recherche, HttpServletRequest request, Livre livre) {

		HttpSession session = request.getSession();
		String motDePasse = (String) session.getAttribute("password");
		String mail = (String) session.getAttribute("username");

		List<Types> listTypes = bookService.chargerLesTypesDeRecherches();
		model.addAttribute("listTypes", listTypes);

		int currentPage = page.orElse(0);
		int pageSize = size.orElse(2);
		try {
			Page<Livre> bookPage = bookService.livresRecherche(type, recherche, pageSize, currentPage, mail,
					motDePasse);
			model.addAttribute("bookPage", bookPage);
			int totalPages = bookPage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);

				Utilisateur user = utilisateurService.infosUtilisateur(mail, motDePasse);
				model.addAttribute("role", user.getRoles().getNom());
			}
			model.addAttribute("type", type.get());
			model.addAttribute("recherche", recherche.get());

			return "books";

		} catch (HttpStatusCodeException e) {

			String error = httpService.traiterLesExceptionsApi(e);
			model.addAttribute("error", error);
			return "error";
		}

	}

}
