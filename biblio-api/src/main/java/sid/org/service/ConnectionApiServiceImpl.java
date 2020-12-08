package sid.org.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ConnectionApiServiceImpl implements ConnectionApiService {
	@Autowired
	private HttpService httpService;
	@Value("${api.url}")
	private String apiUrl;
	@Value("${spring.api.identifiant}")
	private String mail;
	@Value("${spring.api.motDePasse}")
	private String motDePasse;

	@Override
	public void connectApiTimer(Long idPret) {
		RestTemplate rt = new RestTemplate();
		final String uri = apiUrl + "/timer";

		HttpHeaders headers = httpService.creerHeadersHttpAuthentifie(mail, motDePasse);

		ResponseEntity<Long> idPrets = rt.exchange(uri, HttpMethod.POST, new HttpEntity<>(idPret, headers), Long.class);
	}

}
