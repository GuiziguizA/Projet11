package sid.org.batch;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class MaTask extends TimerTask {
	private static final Logger logger = LoggerFactory.getLogger(MaTask.class);

	private Long idPret;
	@Autowired
	private HttpService httpService;
	@Autowired
	private PretBatchService pretBatchService;
	Timer timer;
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

		System.out.println("Timer task started at:" + new Date());
		completeTask();
		System.out.println("Timer task finished at:" + new Date());

	}

	private void completeTask() {

		try {
			pretBatchService.connectPretEnAttente((long) 6);
			logger.info("task est fonctionnel");

		} catch (Exception e) {
			logger.info(getIdPret().toString());
			logger.info("task n'a pas reussi a ce connecter a l'api :" + e);

		}
	}

	public Long getIdPret() {
		return idPret;
	}

	public void setIdPret(Long idPret) {
		this.idPret = idPret;
	}

}
