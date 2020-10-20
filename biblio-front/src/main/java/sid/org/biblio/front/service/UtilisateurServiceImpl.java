package sid.org.biblio.front.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import sid.org.biblio.front.classe.Livre;
import sid.org.biblio.front.classe.Sessions;
import sid.org.biblio.front.classe.Utilisateur;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

	
	@Autowired
	private HttpService httpService;
	
	@Value("${api.url}")
	private String apiUrl;
	/**
	 * creer un utilisateur
	 * @param utilisateur
	 * 
	 */
	@Override
	public void creerUtilisateur(Utilisateur utilisateur) throws HttpStatusCodeException {

		final String uri = apiUrl + "/users";
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers=new HttpHeaders();
	
	
		 rt.exchange(uri, HttpMethod.POST, new HttpEntity<>(utilisateur, headers),
					Utilisateur.class);
		

	}
	/**
	 * recuperer les infos utilisateurs
	 * @param mail
	 * @param motDePasse
	 * @return Utilisateur
	 */
	
	@Override
	public Utilisateur infosUtilisateur(String mail,String motDePasse) {
		final String uri = apiUrl + "/user?mail="+mail;
	
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers =httpService.creerHeadersHttpAuthentifie(mail, motDePasse);
		
		try {
		ResponseEntity<Utilisateur> user = 	 rt.exchange(uri, HttpMethod.GET, new HttpEntity<>( headers),
						Utilisateur.class);
		return user.getBody();
		} catch (HttpStatusCodeException e) {
		return null;
		}
		
		
		
		
	}

}
