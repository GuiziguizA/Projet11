package sid.org.biblio.front.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;

import sid.org.biblio.front.classe.Livre;
import sid.org.biblio.front.enumeration.Types;

public interface BookService {

	

	public void createLivre(Livre livre, String mail, String motDePasse) throws HttpStatusCodeException;

	public Page<Livre> livresRecherche(Optional<String> type, Optional<String> recherche, int size, int page, String mail,
			String motDePasse);


	public List<Types> chargerLesTypesDeRecherches();





	
	
	

	

	

}
