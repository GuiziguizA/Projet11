package sid.org.service;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;


import org.springframework.data.domain.Page;

import sid.org.classe.Pret;
import sid.org.classe.Utilisateur;
import sid.org.dto.PretDto;
import sid.org.exception.BibliothequeException;
import sid.org.exception.EntityAlreadyExistException;
import sid.org.exception.LivreIndisponibleException;
import sid.org.exception.ResultNotFoundException;

public interface PretService {

	
	public 	Pret creerPret(Long idLivre, String mail) throws ResultNotFoundException, LivreIndisponibleException, EntityAlreadyExistException;

	public void supprimerPret(Long id) throws  ResultNotFoundException;

	public Page<Pret> afficherPrets(String mail, int page, int size) throws  ResultNotFoundException;
	
	public Pret afficherUnPret(Long id) throws ResultNotFoundException;

	public List<Pret> afficherPrets() throws ResultNotFoundException;
	public List<Pret> afficherPrets(String statut) throws ResultNotFoundException;
	public void modifierStatut(Long id) throws ResultNotFoundException;
	public void modifierStatutsPrets() throws ResultNotFoundException ;

	public void modifierPret(Long id, String methode) throws ResultNotFoundException;
	

	
	

	
	
	
	
}
