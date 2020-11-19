package sid.org.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import sid.org.classe.Pret;
import sid.org.exception.BadException;
import sid.org.exception.EntityAlreadyExistException;
import sid.org.exception.LivreIndisponibleException;
import sid.org.exception.ResultNotFoundException;

public interface PretService {

	public void creerPret(Long idLivre, String mail)
			throws ResultNotFoundException, LivreIndisponibleException, EntityAlreadyExistException;

	public void supprimerPret(Long id, Optional<String> statutPret) throws ResultNotFoundException, BadException;

	public Page<Pret> afficherPrets(String mail, int page, int size) throws ResultNotFoundException;

	public Pret afficherUnPret(Long id) throws ResultNotFoundException;

	public List<Pret> afficherPrets() throws ResultNotFoundException;

	public List<Pret> afficherPrets(String statut) throws ResultNotFoundException;

	public void modifierStatut(Long id) throws ResultNotFoundException;

	public void modifierStatutsPrets() throws ResultNotFoundException;

	public void modifierPret(Long id, String methode) throws ResultNotFoundException, EntityAlreadyExistException;

	public void verifierPrêt(Long idPret) throws ResultNotFoundException, EntityAlreadyExistException, BadException;

	public void connectApiTimer(Long idPret);

	Optional<Pret> trouverPretenCours(List<Pret> prets);

}