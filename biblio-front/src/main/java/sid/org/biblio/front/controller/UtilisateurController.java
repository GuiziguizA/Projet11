package sid.org.biblio.front.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpStatusCodeException;

import sid.org.biblio.front.classe.Sessions;
import sid.org.biblio.front.classe.Utilisateur;
import sid.org.biblio.front.service.HttpService;
import sid.org.biblio.front.service.UtilisateurService;

@Controller
public class UtilisateurController {
	@Autowired
	private UtilisateurService utilisateurService;
	@Autowired
	private HttpService httpService;
	@GetMapping("/users/form")
	public String afficherForm(Utilisateur utilisateur) {

		return "formulaireUtilisateur";

	}

	@PostMapping("/users")
	public String creerUtilisateur(Utilisateur utilisateur, BindingResult result,Model model) {
		try {
			utilisateurService.creerUtilisateur(utilisateur);
			String succes="Votre compte a ete cree";
			model.addAttribute("succes",succes);
			return "login";
		} catch (HttpStatusCodeException e) {
			String error=httpService.traiterLesExceptionsApi(e);
			model.addAttribute("error", error);
			return  "formulaireUtilisateur";
		}

	}
	
	
	
	@GetMapping("/login")
	public String afficherLogin() {
	
		return "login";

	}
	
}
