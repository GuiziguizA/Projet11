package sid.org.service;




import java.util.Optional;

import org.springframework.data.domain.Page;

import sid.org.classe.Roles;
import sid.org.classe.Sessions;
import sid.org.classe.Utilisateur;
import sid.org.dto.UtilisateurDto;

import sid.org.exception.EntityAlreadyExistException;
import sid.org.exception.MotDePasseInvalidException;
import sid.org.exception.ResultNotFoundException;

public interface UtilisateurService {
	
	public Utilisateur creerUtilisateur(UtilisateurDto utilisateurDto, String role) throws EntityAlreadyExistException;
	public	Utilisateur modifierUtilisateur(Long id, String motDePasse) throws ResultNotFoundException;
	public void supprimerUtilisateur(Long id) throws ResultNotFoundException;
	public Page<Utilisateur> voirListeUtilisateurs(int page, int size) throws  ResultNotFoundException;
	public Utilisateur voirUtilisateur(String mail) throws ResultNotFoundException;
	public Optional<Utilisateur> connectionUtilisateur(Sessions sessions) throws ResultNotFoundException, MotDePasseInvalidException;
	
	
	
	
	
	
	
	
	
}
