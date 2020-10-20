package sid.org.biblio.front.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import sid.org.biblio.front.classe.Pret;

@Service
public class PretServiceImpl implements PretService {

	
	@Autowired
	private HttpService httpService;
	
	
	@Value("${api.url}")
	private String apiUrl;
/**
 * afficher les prets d'un utilisateurs
 * @param  mail
 * @param page
 * @param  size 
 * @param motDePasse
 * @return prets
 * 
 */
	@Override
	public Page<Pret> pretsUtilisateur(String mail, int page, int size,String motDePasse) throws HttpStatusCodeException {

		RestTemplate rt = new RestTemplate();
		String page1 = Integer.toString(page);
		String size1 = Integer.toString(size);
		final String uri = apiUrl + "/prets?mail=" + mail + "&page=" + page1 + "&size=" + size1;
		ParameterizedTypeReference<RestReponsePage<Pret>> responseType = new ParameterizedTypeReference<RestReponsePage<Pret>>() {
		};

		HttpHeaders headers =httpService.creerHeadersHttpAuthentifie(mail, motDePasse);

		ResponseEntity<RestReponsePage<Pret>> result = rt.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers),
				responseType);

		Page<Pret> prets = result.getBody();

		return prets;
	}
	/**
	 * creer un pret 
	 * @param Pret pret
	 * @param String mail
	 * @param String motDePasse
	 * 
	 * 
	 */
	@Override
	public void creerPret(Pret pret,String mail,String motDePasse)throws HttpStatusCodeException{

		RestTemplate rt = new RestTemplate();

		final String uri = apiUrl + "/prets?mail="+mail;

		HttpHeaders headers =httpService.creerHeadersHttpAuthentifie(mail, motDePasse);

		rt.exchange(uri, HttpMethod.POST, new HttpEntity<>(pret, headers), Pret.class);

	}
	/**
	 * modifier le statut d'un pret et incrementation du stock de livre
	 * @param long id
	 * @param String mail
	 * @param String motDePasse
	 * 
	 * 
	 */
@Override
public void modifierUnPret(Long Id,String mail,String motDePasse) throws HttpStatusCodeException{
	RestTemplate rt = new RestTemplate();

	final String uri = apiUrl + "/prets?id="+Id;
	

	HttpHeaders headers =httpService.creerHeadersHttpAuthentifie(mail, motDePasse);
	rt.exchange(uri, HttpMethod.PUT, new HttpEntity<>( headers), Long.class);

}
/**
 * renouveler un pret 
 * @param long id
 * @param String mail
 * @param String motDePasse
 * 
 * 
 */
@Override
public void renouvelerUnPret(Long Id,String mail,String motDePasse) throws HttpStatusCodeException{
	RestTemplate rt = new RestTemplate();

	final String uri = apiUrl + "/pret?id="+Id;
	

	HttpHeaders headers =httpService.creerHeadersHttpAuthentifie(mail, motDePasse);
	rt.exchange(uri, HttpMethod.PUT, new HttpEntity<>( headers), Long.class);

}
/**
 * afficher tous les prets
 * @param String mail
 * @param int page
 * @param int size 
 * @param String motDePasse
 * @return Page<Pret>prets
 * 
 */
@Override
public Page<Pret> AfficherToutLesPrets(int page,int size,String mail,String motDePasse)throws HttpStatusCodeException {
	
	RestTemplate rt = new RestTemplate();
	final String uri = apiUrl + "/allprets?page=0&size=2";
	

	
	
	HttpHeaders headers =httpService.creerHeadersHttpAuthentifie(mail, motDePasse);
	ParameterizedTypeReference<List<Pret>> responseType = new ParameterizedTypeReference<List<Pret>>() {
	};
	ResponseEntity<List<Pret>> pret = rt.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), responseType);
	List<Pret>prets= pret.getBody();
	Pageable pageable=PageRequest.of(page,size );
	int start = (int) pageable.getOffset();
	int end = (start + pageable.getPageSize()) > prets.size() ? prets.size() : (start + pageable.getPageSize());
	Page<Pret> pages = new PageImpl<Pret>(prets.subList(start, end), pageable, prets.size());
	
	return pages;
}




}
