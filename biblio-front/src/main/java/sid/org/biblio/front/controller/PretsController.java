package sid.org.biblio.front.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import sid.org.biblio.front.classe.Pret;
import sid.org.biblio.front.classe.Utilisateur;
import sid.org.biblio.front.service.HttpService;
import sid.org.biblio.front.service.PretService;
import sid.org.biblio.front.service.UtilisateurService;

@Controller
public class PretsController {
	@Autowired
	private PretService pretService;
	@Autowired
	private UtilisateurService utilisateurService;
	@Autowired
	private HttpService httpService;
	
	
	
	 @Secured(value= {"ROLE_user"}) 
	@GetMapping(value = "/prets")
	public String pretUtilisateur(Model model, @RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size,HttpServletRequest request) {
		int currentPage = page.orElse(0);
		int pageSize = size.orElse(2);
		HttpSession session = request.getSession();
		String motDePasse=(String) session.getAttribute("password");
		String mail=(String) session.getAttribute("username");

		try {
			Page<Pret> prets = pretService.pretsUtilisateur(mail, currentPage, pageSize,motDePasse);
			model.addAttribute("prets", prets);
			
			Utilisateur user=utilisateurService.infosUtilisateur(mail,motDePasse );
			model.addAttribute("role",user.getRoles().getNom());
			int totalPages = prets.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}

		} catch (HttpStatusCodeException e) {

			String error=httpService.traiterLesExceptionsApi(e);
			model.addAttribute("error", error);
		}

		return "prets";
	}

	@PostMapping(value = "/prets", consumes = "application/x-www-form-urlencoded")
	public String creerUnPret(@RequestParam Long id, Model model,Principal principal,HttpServletRequest request) throws Exception {
		Pret pret = new Pret();
		pret.setId(id);
		
		HttpSession session = request.getSession();
		String motDePasse=(String) session.getAttribute("password");
		String mail=(String) session.getAttribute("username");
		try {
			pretService.creerPret(pret,mail,motDePasse);
			return "succesOperation";
		} catch (HttpStatusCodeException e) {

			String error=httpService.traiterLesExceptionsApi(e);
			model.addAttribute("error", error);
			return "error";
		}

	}
	
	@GetMapping(value = "/pretstatut/{id}")
	public String modifierPrets(@PathVariable Long id,Model model,Principal principal,HttpServletRequest request) {
		
		
		HttpSession session = request.getSession();
		String motDePasse=(String) session.getAttribute("password");
		String mail=(String) session.getAttribute("username");
		Utilisateur user=utilisateurService.infosUtilisateur(mail,motDePasse );
		model.addAttribute("role",user.getRoles().getNom());
		try {
			pretService.modifierUnPret(id, mail, motDePasse);
		
			return "succesOperation";
		} catch (HttpStatusCodeException e) {
			String error=httpService.traiterLesExceptionsApi(e);
			model.addAttribute("error", error);
			return"error";
		}
	}
	
	@GetMapping(value = "/prolongerPret/{id}")
	public String renouvelerPrets(@PathVariable Long id,Model model,Principal principal,HttpServletRequest request) {
		
		
		HttpSession session = request.getSession();
		String motDePasse=(String) session.getAttribute("password");
		String mail=(String) session.getAttribute("username");
		Utilisateur user=utilisateurService.infosUtilisateur(mail,motDePasse );
		model.addAttribute("role",user.getRoles().getNom());
		try {
			pretService.renouvelerUnPret(id, mail, motDePasse);
		
			return "succesOperation";
		} catch (HttpStatusCodeException e) {
			String error=httpService.traiterLesExceptionsApi(e);
			model.addAttribute("error", error);
			return"error";
		}
	}
	
	 @Secured(value= {"ROLE_admin","ROLE_employe"}) 
		@GetMapping(value = "/allprets")
		public String TousLesPrets(Model model, @RequestParam("page") Optional<Integer> page,
				@RequestParam("size") Optional<Integer> size,HttpServletRequest request) {
			int currentPage = page.orElse(0);
			int pageSize = size.orElse(2);
			HttpSession session = request.getSession();
			String motDePasse=(String) session.getAttribute("password");
			String mail=(String) session.getAttribute("username");

			try {
				Page<Pret> prets = pretService.AfficherToutLesPrets(currentPage, pageSize, mail, motDePasse);
				model.addAttribute("prets", prets);
				
				Utilisateur user=utilisateurService.infosUtilisateur(mail,motDePasse );
				model.addAttribute("role",user.getRoles().getNom());
				int totalPages = prets.getTotalPages();
				if (totalPages > 0) {
					List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
					model.addAttribute("pageNumbers", pageNumbers);
				}

			} catch (HttpStatusCodeException e) {
				String error=httpService.traiterLesExceptionsApi(e);
				model.addAttribute("error", error);
			}

			return "prets";
		}
	
		
		
	 
	 
	 
	 
	 
	}
	
	
