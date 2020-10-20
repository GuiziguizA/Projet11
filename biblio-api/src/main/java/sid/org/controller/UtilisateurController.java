package sid.org.controller;


import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import sid.org.classe.Sessions;
import sid.org.classe.Utilisateur;
import sid.org.dto.UtilisateurDto;

import sid.org.exception.EntityAlreadyExistException;
import sid.org.exception.MotDePasseInvalidException;
import sid.org.exception.ResultNotFoundException;
import sid.org.service.UtilisateurService;

@RestController
@Api(value="Api Utilisateurs",description = "Api Utilisateurs")
public class UtilisateurController {
	
	@Value("${role.default}") 
	 private String roleDefault;
	@Autowired
	private UtilisateurService utilisateurService;

	
	@GetMapping("/user")
	@ApiOperation(value="affiche un utilisateur en fonction de son id",response = UtilisateurController.class)
 public Utilisateur afficherUtilisateurs(@RequestParam String mail) throws ResultNotFoundException {
	  
	Utilisateur user=utilisateurService.voirUtilisateur(mail);
		return user;
	

}
	
	
	@PostMapping("/users/identity")
	@ApiOperation(value="identification de l'utilisateur",response = UtilisateurController.class)
 public Optional<Utilisateur> afficherUtilisateurs(@RequestBody Sessions sessions) throws ResultNotFoundException,MotDePasseInvalidException {
	  
	Optional<Utilisateur> user=utilisateurService.connectionUtilisateur(sessions);
		return user;
	

}
	
	
	
	
	
	@GetMapping("/users")
	@ApiOperation(value="affiche une Page avec tous les utilisateurs",response = UtilisateurController.class)
	 public   Page<Utilisateur> afficherUtilisateurs(@RequestParam int page, @RequestParam int size) throws ResultNotFoundException{
		  
		  Page<Utilisateur> users;
		 
			users=utilisateurService.voirListeUtilisateurs(page,size);
			return  users; 
			

	}
	@PostMapping("/users")
	@ApiOperation(value="ajouter un utilisateur ",response = UtilisateurController.class)
	public Utilisateur creerUtilisateur(
			  @ApiParam(value="Ajouter UtilisateurDto dans le body" , required=true)@Valid@RequestBody UtilisateurDto utilisateurDto) throws ResultNotFoundException,EntityAlreadyExistException{

		Utilisateur user =	utilisateurService.creerUtilisateur(utilisateurDto,roleDefault);
		return user;
		

	}
	
	@PutMapping("/users/{id}")
	@Secured(value= {"ROLE_admin"}) 
	@ApiOperation(value="modifier le statut de l'utilisateur",response = UtilisateurController.class)
	public Utilisateur modifierUtilisateur(@PathVariable Long id ,   @ApiParam(value="statut dans le body" , required=true)@Valid @RequestBody String statut) throws ResultNotFoundException{
		
		Utilisateur user =	utilisateurService.modifierUtilisateur(id, statut);
		return user;
		

	}
	
	@Secured(value= {"ROLE_admin"}) 
	@DeleteMapping("/users/{id}")
	 @ApiOperation(value="supprimer un utilisateur",response = UtilisateurController.class)
	public void supprimerUtilisateur(@PathVariable Long id) throws ResultNotFoundException	{
		
		

			utilisateurService.supprimerUtilisateur(id);
	
	
	}
	 
	

	
}