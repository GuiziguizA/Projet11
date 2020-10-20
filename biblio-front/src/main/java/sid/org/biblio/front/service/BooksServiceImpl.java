package sid.org.biblio.front.service;

import java.io.IOException;

import java.net.URL;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.Nullable;

import groovy.util.logging.Slf4j;
import sid.org.biblio.front.classe.Livre;
import sid.org.biblio.front.classe.LivreCriteria;
import sid.org.biblio.front.config.RequestFactory;
import sid.org.biblio.front.config.SimpleAuthenticationFilter;
import sid.org.biblio.front.enumeration.ListType;
import sid.org.biblio.front.enumeration.Types;

@Component
@Slf4j
public class BooksServiceImpl implements BookService {

	private final RequestFactory requestFactory;

	@Autowired
	public BooksServiceImpl(RequestFactory requestFactory) {
		this.requestFactory = requestFactory;
	}
	@Autowired
	private HttpService httpService;
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${api.url}")
	private String apiUrl;
	

/*
 * Creation d'un Livre
 * @param Livre
 * @param String mail
 * @param String motDePasse
 * 
 * 
 */
	


	@Override
	public void createLivre(Livre livre,String mail,String motDePasse) throws HttpStatusCodeException {

		RestTemplate rt = new RestTemplate();
		final String uri = apiUrl + "/books";
		
		
		
		HttpHeaders headers =httpService.creerHeadersHttpAuthentifie(mail, motDePasse);
		
			ResponseEntity<Livre> livres = rt.exchange(uri, HttpMethod.POST, new HttpEntity<>(livre, headers),
					Livre.class);
		

	}
	/*
	 * affiche une pages contenant des livres en fonctiond'un critère
	 * @param Optional<String> type
	 * @param Optional<String> recherche
	 * @param int size
	 * @param int page
	 * @param String mail
	 * @param String motDePasse
	 * @return Page<Livre> bookPage
	 */
	@Override
	public Page<Livre> livresRecherche(Optional<String> type, Optional<String> recherche, int size, int page,String mail,String motDePasse)throws HttpStatusCodeException {

		String page1 = Integer.toString(page);
		String size1 = Integer.toString(size);
		RestTemplate rt = requestFactory.getRestTemplate();
		HttpHeaders headers =httpService.creerHeadersHttpAuthentifie(mail, motDePasse);
		ParameterizedTypeReference<RestReponsePage<Livre>> responseType = new ParameterizedTypeReference<RestReponsePage<Livre>>() {
		};
		final String uri = apiUrl + "/books?page=" + page1 + "&size=" + size1;
		LivreCriteria critere = critereImpl(type, recherche);

			ResponseEntity<RestReponsePage<Livre>> result =  rt.exchange(uri, HttpMethod.GET,
					new HttpEntity<>(critere, headers), responseType);
			Page<Livre> bookPage = result.getBody();
			return bookPage;
		
		
	
	
	}
	/*
	 * creation d'un livreCriteria
	 * @param Optional<String> type
	 * @param Optional<String> recherche
	 * @return LivreCriteria
	 * 
	 */
	public LivreCriteria critereImpl(Optional<String> type, Optional<String> recherche) {
		LivreCriteria critere = new LivreCriteria();

		if (type.get().equals("nom") && recherche != null) {
			critere.setNom(recherche.get());
		}
		if (type.get().equals("auteur") && recherche != null) {
			critere.setAuteur(recherche.get());
		}
		if (type.get().equals("type") && recherche != null) {
			critere.setType(recherche.get());
		}

		return critere;

	}
	
	
/**
 * charger les différents types correspondant au différent champs de recherches des livres 
 */
	@Override
	public List<Types> chargerLesTypesDeRecherches(){
		
		ListType listType=new ListType();
		List<Types>types=listType.listTypes();
		
		return types;
		
	}

	
	
		
	
}
