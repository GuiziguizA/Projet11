package sid.org.biblio.front.service;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.HttpStatusCodeException;

public interface HttpService {

	public HttpHeaders creerHeadersHttpAuthentifie(String mail, String motDePasse);

	public String traiterLesExceptionsApi(HttpStatusCodeException error);

}
