package sid.org.service;


import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import sid.org.classe.Livre;
import sid.org.classe.Pret;
import sid.org.classe.Utilisateur;
import sid.org.dao.LivreRepository;
import sid.org.dao.PretRepository;
import sid.org.dao.UtilisateurRepository;
import sid.org.dto.PretDto;
import sid.org.enumeration.statutPret;
import sid.org.exception.BibliothequeException;
import sid.org.exception.EntityAlreadyExistException;
import sid.org.exception.LivreIndisponibleException;
import sid.org.exception.ResultNotFoundException;

@Service
public class PretServiceImpl implements PretService{
	
	@Autowired
	private PretRepository pretRepository;
	@Autowired
	private DateService dateService;
	@Autowired
	private UtilisateurRepository utilisateurRepository;
	@Autowired
	private LivreRepository livreRepository;
	
	
	  @Value("${pret.statut1}") 
	  private String encours;
	  
	  @Value("${pret.statut2}") 
	  private String prolonge;
	  
	  @Value("${pret.statut3}") 
	  private String depasse;
	  
	  @Value("${pret.statut4}") 
	  private String remis;
	  
	  @Value("${pret.time}") 
	  private int time;
	 
	 
	 

	  
		/*
		 *Creation d'un Pret + decrementation nombreExemplaire pour le livre emprunté
		 * @param Long idLivre
		 * @param String mail
		 *
		 * @return Pret
		 */

	@Override
	@Transactional
	public Pret creerPret(Long idLivre,String mail) throws ResultNotFoundException,LivreIndisponibleException, EntityAlreadyExistException {
		
		Optional<Utilisateur> utilisateur = utilisateurRepository.findByMail(mail);
		Optional<Livre>livre=livreRepository.findByCodeLivre(idLivre);
		
		if(!utilisateur.isPresent()) {
			throw new ResultNotFoundException("l'utilisateur n'existe pas");
		}
		if(!livre.isPresent()) {
			throw new ResultNotFoundException("le livre n'existe pas");
		}

		if(livre.get().getNombreExemplaire()< 1) {
			throw new LivreIndisponibleException("ce livre n'est pas disponible pour "+livre.get().getNombreExemplaire()+" exemplaires");
		}
		
		List<Pret>listpret=pretRepository.findByUtilisateurAndLivre(utilisateur.get(),livre.get());
		Optional<Pret>pret1= trouverPretenCours(listpret);
		
		
		if(pret1.isPresent()&&!pret1.get().getStatut().equals(remis)) {
			throw new EntityAlreadyExistException("La reservation existe deja pour ce livre");
		}
		
		
		Date date1=new Date();
		
		Pret pret = new Pret();
		pret.setLivre(livre.get());
		pret.setDateDeDebut(date1);
		pret.setDateDeFin(dateService.modifierDate(date1,time ));
		pret.setUtilisateur(utilisateur.get());
		pret.setNombreLivres(1);
		pret.setStatut(encours);
		livre.get().setNombreExemplaire(livre.get().getNombreExemplaire()-1);
		livreRepository.saveAndFlush(livre.get());
		return pretRepository.saveAndFlush(pret);
	}

	/*
	 *Trouver le pret avec statut encours dans une list de pret
	 * @param List<Pret>prets
	 * 
	 *
	 * @return Optional<Pret>
	 */
	public Optional<Pret> trouverPretenCours(List<Pret>prets){
		
	List<Pret> listPret=new ArrayList<Pret>();
		 for (Pret pret : prets) {
		        if (pret.getStatut().equals(remis)) {
		           
		        }else {
		        	listPret.add(pret);
		        }
		    	
	}
		 if(!listPret.isEmpty()) {
			 Optional<Pret> pret1=Optional.of(listPret.get(0)); 
			 return pret1;
		 }else {
			 Optional<Pret> pret1=Optional.empty();
			 return pret1;
		}
		
		
	}
	
	
	/*
	 *SupprimerPret
	 * @param Long Id
	 * 
	 *
	 *
	 */
	@Transactional
	@Override
	public void supprimerPret(Long id) throws ResultNotFoundException {
		Optional<Pret> pret=pretRepository.findById(id);
		
		if(!pret.isPresent()) {
			throw new ResultNotFoundException("ce pret n'existe pas");
		}
		Optional<Livre> livre=livreRepository.findById(pret.get().getLivre().getCodeLivre());
		if(!pret.get().getStatut().equals(remis)) {
		livre.get().setNombreExemplaire(livre.get().getNombreExemplaire()+pret.get().getNombreLivres());}
		livreRepository.saveAndFlush(livre.get());
		pretRepository.delete(pret.get());
	
	}

	
	
	/*
	 *Afficher Page de prets en fonction d'un e-mail
	 * @param String mail
	 * @param int page
	 * @param int size
	 *
	 *@return Page<Pret>
	 */
	
	
	@Override
	public Page<Pret> afficherPrets(String mail,int page,int size) throws ResultNotFoundException  {
		if(size==0) {
			throw new ResultNotFoundException();
		}
		Optional<Utilisateur> utilisateur=utilisateurRepository.findByMail(mail);
		if(!utilisateur.isPresent()) {
			throw new ResultNotFoundException("l'utilisateur est introuvable");
		}
		
		Pageable pageable=PageRequest.of(page, size);
		Page<Pret>listPretUtilisateur=pretRepository.findByUtilisateur(utilisateur.get(),pageable);

		
		return listPretUtilisateur;
	}

	/*
	 *Afficher la Liste de tous lesPrets
	 * @param Long id 
	 * 
	 *
	 *@return List<Pret>prets
	 */
	@Override
	public  List<Pret> afficherPrets() throws ResultNotFoundException{
		List<Pret> prets=pretRepository.findAll();
		

	
			return prets;
		
	}
	/*
	 *Afficher une liste de Prets en fonction d'un statut
	 * @param Long id 
	 * 
	 *
	 *@return List<Pret>prets
	 */
	@Override
	public  List<Pret> afficherPrets(String statut) throws ResultNotFoundException{
		List<Pret> prets=pretRepository.findByStatut(statut);
		

	
			return prets;
		
	}
	/*
	 *Afficher Un pret
	 * @param Long id 
	 * 
	 *
	 *@return Pret
	 */
	@Override
	public Pret afficherUnPret(Long id)throws ResultNotFoundException{
		Optional<Pret> pret=pretRepository.findById(id);
		if(!pret.isPresent()) {
			throw new ResultNotFoundException("ce pret n'existe pas");
		}
		return pret.get();
	}

	/*
	 *Modifier le statut d'un Pret en statut3 uniquement si le pret a un statut1 ou statut2
	 * @param Long id 
	 * 
	 *
	 *
	 */
	 
	@Override
	 public void modifierStatut(Long id) throws ResultNotFoundException {
	 	 Date aujourdhui = new Date(); 
	 	 Optional<Pret> pret =pretRepository.findById(id);
	 	 if(pret.get().getDateDeFin().compareTo(aujourdhui)>0 && pret.get().getStatut()==prolonge) {
	 		 pret.get().setStatut(depasse);
	 		 pretRepository.saveAndFlush(pret.get());
	 	 }else if (pret.get().getDateDeFin().compareTo(aujourdhui)>0 && pret.get().getStatut()==encours) {
	 		 pret.get().setStatut(depasse);
	 		 pretRepository.saveAndFlush(pret.get());
	 	 }
	 }
	
	
	/*
	 *Appliquer la methode modifier Statut sur tout les pret si la da de fin de pret est depassé
	 * 
	 * 
	 *
	 *
	 */
@Override
public void modifierStatutsPrets() throws ResultNotFoundException {
	
	ArrayList<Pret>prets=(ArrayList<Pret>)afficherPrets();
	if (prets.isEmpty()) {
	for(int i = 0; i <prets.size(); i++) {
		modifierStatut(prets.get(i).getId());
	}
	}
	}

/*
 *Modifier le statut d'un Pret en statut remis  ou statut prolonge en fonction du String methode
 * @param id du pret
 * @param String methode equals a remise on
 *
 *
 */
@Override
public void modifierPret(Long id,String methode) throws ResultNotFoundException{
	Optional<Pret>pret=pretRepository.findById(id);
	
	if(!pret.isPresent()) {
		throw new ResultNotFoundException("Ce pret n'existe pas");
	}
	Optional<Livre>livre=livreRepository.findByCodeLivre(pret.get().getLivre().getCodeLivre());
	if(!livre.isPresent()) {
		throw new ResultNotFoundException("Ce livre n'existe pas");
	}
	if(methode.equals("remise")) {
	pret.get().setStatut(remis);
	pret.get().setDateDeRendu(new Date());
	}
	else {
	pret.get().setStatut(prolonge);
	pret.get().setDateDeFin(dateService.modifierDate(pret.get().getDateDeFin(), time));
	}
	
	
	if(methode.equals("remise")) {
	livre.get().setNombreExemplaire(livre.get().getNombreExemplaire() + 1);
	}
	pretRepository.saveAndFlush(pret.get());
	livreRepository.saveAndFlush(livre.get());
}




	
}
