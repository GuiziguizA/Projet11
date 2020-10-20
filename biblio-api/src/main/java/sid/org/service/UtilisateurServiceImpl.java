package sid.org.service;



import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import sid.org.classe.Pret;
import sid.org.classe.Roles;
import sid.org.classe.Sessions;
import sid.org.classe.Utilisateur;
import sid.org.dao.PretRepository;
import sid.org.dao.RolesRepository;
import sid.org.dao.UtilisateurRepository;
import sid.org.dto.UtilisateurDto;
import sid.org.exception.BibliothequeException;
import sid.org.exception.EntityAlreadyExistException;
import sid.org.exception.MotDePasseInvalidException;
import sid.org.exception.ResultNotFoundException;


@Component
public class UtilisateurServiceImpl implements UtilisateurService {
	@Autowired
	private UtilisateurRepository utilisateurRepository;

	@Autowired
	private PretRepository pretRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RolesRepository rolesRepository;
	 
/**
 * creation d'un utilisateur 
 * @param  utilisateurDto
 * @param  role
 * 
 * @return Utilisateur
 */
	@Override
	public Utilisateur creerUtilisateur(UtilisateurDto utilisateurDto,String role) throws EntityAlreadyExistException{
		Optional<Utilisateur> user =utilisateurRepository.findByMail(utilisateurDto.getMail());
		if(user.isPresent()) {
			throw new EntityAlreadyExistException("le mail est deja utilise");
		}
		
	
		utilisateurDto.setMotDePasse(passwordEncoder.encode(utilisateurDto.getMotDePasse()));
		Utilisateur utilisateur= convertToUtilisateur(utilisateurDto);
		Optional<Roles> roles=rolesRepository.findByNom(role);
		utilisateur.setRoles(roles.get());
		return utilisateurRepository.save(utilisateur);
	}
	/**
	 * Modifier le statut d'un utilisateur
	 * @param id
	 * @param  statut
	 * 
	 * @return Utilisateur
	 */
	@Override
	public Utilisateur modifierUtilisateur(Long id, String statut) throws ResultNotFoundException{
		Optional<Utilisateur> user =utilisateurRepository.findById(id);
		
		if(!user.isPresent()) {
			throw new ResultNotFoundException("Utilisateur introuvable");
		}
		Roles role = new Roles();
		role.setNom(statut);
		user.get().setRoles(role);
		
		return utilisateurRepository.save(user.get());
	}
	/**
	 * Supprimer un utilisateur
	 * @param  id
	 * 
	 * 
	 * 
	 */
	@Override
	@Transactional
	public void supprimerUtilisateur(Long id) throws ResultNotFoundException {
Optional<Utilisateur> user =utilisateurRepository.findById(id);

Pageable pageable=PageRequest.of(2, 5);
Page<Pret>listPretUtilisateur=pretRepository.findByUtilisateur(user.get(),pageable);		
		if(!user.isPresent()) {
			throw new ResultNotFoundException("Utilisateur introuvable");
		}
	
		utilisateurRepository.delete(user.get());
		pretRepository.deleteAll(listPretUtilisateur);
		
	}
	/**
	 * afficher un utilisateur 
	 * @param  mail
	 * 
	 * 
	 * @return Utilisateur
	 */
	@Override
	public Utilisateur voirUtilisateur(String mail) throws ResultNotFoundException{
		

		
		Optional<Utilisateur>user= utilisateurRepository.findByMail(mail);
		if(!user.isPresent()) {
			throw new ResultNotFoundException("Utilisateur introuvable");
		}
		
	
	
	return user.get();
	}
	/**
	 * Afficher une Page d'utilisateurs 
	 *  @param  page 
	 *  @param size
	 * 
	 * 
	 *  @return une page contenant des Utilisateurs
	 */
	@Override
	public Page<Utilisateur> voirListeUtilisateurs(int page, int size) throws ResultNotFoundException{
		if(size==0) {
			throw new ResultNotFoundException();
		}
Pageable pageable =PageRequest.of(page,size );
		  Page<Utilisateur> utilisateurs=utilisateurRepository.findAll(pageable);
		  
		
	
	return utilisateurs;
	}
	/**
	 * return un utilisateur en fonction du mail de l'objet Sessions
	 *@param  sessions
	 * 
	 *@return Utilisateur
	 */
	
	@Override
	public Optional<Utilisateur> connectionUtilisateur(Sessions sessions) throws ResultNotFoundException, MotDePasseInvalidException {
		Optional<Utilisateur>user=utilisateurRepository.findByMail(sessions.getMail());
		
		
		if(!user.isPresent()) {
			throw new ResultNotFoundException("Il n'existe aucun compte contenant cette adresse e-mail");
		}
	if(!passwordEncoder.matches(sessions.getMotDePasse(), user.get().getMotDePasse())) {
		throw new MotDePasseInvalidException("mot de passe invalide");
	}
		
		
		return user;
	
		
	}
	
	
	
	/**
	 *Convertie un UtilsateurDto en Utilisateur
	 *@param UtilisateurDto
	 * 
	 *@return Utilisateur
	 */
	
private Utilisateur convertToUtilisateur(UtilisateurDto utilisateurDto) {
	Utilisateur utilisateur = new Utilisateur();
	utilisateur.setAdresse(utilisateurDto.getAdresse());
	utilisateur.setCodePostal(utilisateurDto.getCodePostal());
	utilisateur.setMail(utilisateurDto.getMail());
	utilisateur.setMotDePasse(utilisateurDto.getMotDePasse());
	utilisateur.setNom(utilisateurDto.getNom());
	return utilisateur;
	
	
	
}

}
