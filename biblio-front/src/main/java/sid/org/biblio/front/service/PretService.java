package sid.org.biblio.front.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import sid.org.biblio.front.classe.Pret;

public interface PretService {

	public Page<Pret> pretsUtilisateur(String mail, int page, int size, String motDePasse)throws HttpStatusCodeException;

	public void creerPret(Pret pret, String mail, String motDePasse)throws HttpStatusCodeException;

	public void modifierUnPret(Long Id, String mail, String motDePasse)throws HttpStatusCodeException;

	public Page<Pret> AfficherToutLesPrets(int page, int size, String mail, String motDePasse)throws HttpStatusCodeException;

	public void renouvelerUnPret(Long Id, String mail, String motDePasse) throws HttpStatusCodeException;

	

	



}
