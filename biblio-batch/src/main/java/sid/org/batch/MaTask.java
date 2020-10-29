package sid.org.batch;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class MaTask extends TimerTask {
	private static final Logger logger = LoggerFactory.getLogger(MaTask.class);
	private Long idPret;
	@Autowired
	private HttpService httpService;

	@Value("${api.url}")
	private String apiUrl;
	@Value("${spring.api.identifiant}")
	private String mail;
	@Value("${spring.api.motDePasse}")
	private String motDePasse;

	public MaTask(Long idPret) {
		super();
		this.idPret = idPret;
	}

	@Override
	public void run() {

		try {
			this.connectApiTimer(idPret);
			logger.info("task est fonctionnel");

		} catch (Exception e) {
			logger.info("task n'a pas reussi a ce connecter a l'api");
		}

	}

	public void connectApiTimer(Long idPret) {
		RestTemplate rt = new RestTemplate();
		final String uri = apiUrl + "/pretEnAttente";

		HttpHeaders headers = httpService.creerHeadersHttpAuthentifie(mail, motDePasse);

		ResponseEntity<Long> idPrets = rt.exchange(uri, HttpMethod.POST, new HttpEntity<>(idPret, headers), Long.class);
	}
}
