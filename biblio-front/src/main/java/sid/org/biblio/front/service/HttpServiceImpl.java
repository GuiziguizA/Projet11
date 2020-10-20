package sid.org.biblio.front.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
@Service
public class HttpServiceImpl implements HttpService{

	/**
	 * creation du header d'une requete http pour acceder au api
	 */
	@Override
	public HttpHeaders creerHeadersHttpAuthentifie(String mail,String motDePasse) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBasicAuth(mail, motDePasse);
		
		return headers;
	}
	
	/**
	 * afficher uniquelment le message de l'exception
	 * @param  error
	 * @return le message d'erreur
	 */
	
	@Override
	public String traiterLesExceptionsApi(HttpStatusCodeException error) {
		String messageErreur=error.getMessage(); 
		 
		 String[] split=messageErreur.split("message");
		 if(split.length==1) {
			 return "error"; 
		 }else {
		  
		  String[] split1 =split[1].split(":");
		  String[] split2 =split1[1].split(",");
		  String result= split2[0];
		
		  return result.replaceAll("\"",""); }
		 
		
}
}