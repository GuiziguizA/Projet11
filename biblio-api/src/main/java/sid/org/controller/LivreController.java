package sid.org.controller;








import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sid.org.classe.Livre;
import sid.org.dto.LivreDto;
import sid.org.exception.BibliothequeException;
import sid.org.exception.EntityAlreadyExistException;
import sid.org.exception.ResultNotFoundException;
import sid.org.service.LivreService;
import sid.org.specification.LivreCriteria;

@RestController
@Api(value="Api Livres",description = "Api Livres")
public class LivreController {
	@Autowired
	private LivreService livreService;
	
	
	
	  @GetMapping(value="/books", consumes = "application/json",produces = "application/json") 
	  @ApiOperation(value="afficher une page contenant les différent livre de la recherche",response = LivreController.class)
	  @ApiResponses(value = {
	  @ApiResponse(code=200,message="affichage de la recherche")
	  }
		)
	  public Page<Livre>afficherLivres(
			  @ApiParam(value="Ajouter un LivreCriteria dans le body" , required=true)@RequestBody LivreCriteria livreSearch,
			  @ApiParam(value="noter la page" , required=true)@RequestParam int page ,
			  @ApiParam(value="noter la size" , required=true)  @RequestParam int size) throws ResultNotFoundException {
	  
		  Page<Livre> livres;
	  
	
		livres = livreService.searchLivres(livreSearch,page ,size);
		return livres; 
	
	  
	  
	  }
	  
	  
	  @GetMapping("/books/{id}")
	  @ApiOperation(value="afficher un livre en fonction de son id",response = LivreController.class)
public  Livre afficheUnLivre(@PathVariable Long id) throws ResultNotFoundException{
		 
		
	  Livre livre=livreService.afficheUnLivre(id);
	  return livre;
	
	 
	  
	  }
	  
	  
	  @PostMapping("/books")
	 @Secured(value= {"ROLE_admin","ROLE_employe"}) 
	  @ApiOperation(value="ajout d'un nouveau livre",response = LivreController.class)
	public  Livre ajouterLivre(
			  @ApiParam(value="LivreDto dans le body" , required=true)@Valid @RequestBody LivreDto livre) throws EntityAlreadyExistException { 
		  
		  return livreService.createLivre(livre);
		  
	  }
	 
	@Secured(value= {"ROLE_admin"}) 
	  @DeleteMapping("/books")
	  @ApiOperation(value="supprimer un livre",response = LivreController.class)
	  public  void supprimerUnLivre(@RequestParam Long id) throws ResultNotFoundException{
	  		 
	  		
	  	 livreService.supprimerLivre(id);
	  	
	  	 
	  	  
	  	  }
	  
	  

}
