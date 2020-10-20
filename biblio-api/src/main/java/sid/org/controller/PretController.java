package sid.org.controller;



import java.util.List;





import org.springframework.beans.factory.annotation.Autowired;
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
import sid.org.classe.Pret;
import sid.org.dto.PretDto;
import sid.org.exception.EntityAlreadyExistException;
import sid.org.exception.LivreIndisponibleException;
import sid.org.exception.ResultNotFoundException;
import sid.org.service.PretService;


@RestController
@Api(value="Api Prets",description = "Api Prets")
public class PretController {
@Autowired
	 private PretService pretService;




@GetMapping("/prets/{id}")
@ApiOperation(value="affiche un pret en fonction de son id",response = PretController.class)
public Pret afficherUnPret(@PathVariable Long id) throws ResultNotFoundException{
 

	 Pret pret = pretService.afficherUnPret(id);
	return pret;


  
  	}





	
 @PostMapping(value="/prets")	
@ApiOperation(value="ajout d'un nouveau pret et decrementation le stock du livre",response = PretController.class)
 public  Pret creerUnPret(
		 @ApiParam(value="Ajouter int idLivre dans le body" , required=true) @RequestBody PretDto pretDto,@RequestParam String mail) throws ResultNotFoundException,LivreIndisponibleException, EntityAlreadyExistException {
		  
		
	 		Pret pret1=pretService.creerPret(pretDto.getId(),mail);
	 		
	 		
			return pret1 ;
	
	  
	
	  	  
	  	  }

 @Secured(value= {"ROLE_admin","ROLE_employe"}) 
@DeleteMapping("/prets/{id}")
 @ApiOperation(value="supprime le pret et reincremente le stock du livre",response = PretController.class)
public  void supprimerUnPret(@PathVariable Long id) throws ResultNotFoundException{

		pretService.supprimerPret(id);
	
	
}
 	
	
@GetMapping("/prets")
@ApiOperation(value="affiche Les pret d'un utilisateur",response = PretController.class)
public Page<Pret> afficherPretUtilisateur(@RequestParam String mail, @RequestParam int page, @RequestParam int size) throws ResultNotFoundException{
 
Page<Pret>prets=pretService.afficherPrets(mail, page, size);
	
	return prets;


  
  	}

@GetMapping("/allprets")
@ApiOperation(value="Renvoie liste de tout les prets",response = PretController.class)
public List<Pret> afficherPrets() throws ResultNotFoundException{
 
List<Pret>prets=pretService.afficherPrets();
	
	return prets;


  
  	}
	


@GetMapping("/listprets")
@ApiOperation(value="affiche Les prets en fonction d'un statut",response = PretController.class)
public List<Pret> afficherPretUtilisateur(@RequestParam String statut) throws ResultNotFoundException{
 
List<Pret>prets=pretService.afficherPrets(statut);
	
	return prets;


  
  	}

@PutMapping("/prets")
@ApiOperation(value="modifier statut du pret pour confirmer que le livre a été rendu",response = PretController.class)
public void retourLivre(@RequestParam Long id) throws ResultNotFoundException {
	pretService.modifierPret(id,"remise");
	
}

@PutMapping("/pret")
@ApiOperation(value="modifier statut du pret pour renouvelé la periode de pret",response = PretController.class)
public void prolongerLivre(@RequestParam Long id) throws ResultNotFoundException {
	pretService.modifierPret(id,"renouvelement");
	
}


@PutMapping("/prets/statuts")
public  void  modifierStatutBatch() throws ResultNotFoundException {
	
	pretService.modifierStatutsPrets();



}



}
