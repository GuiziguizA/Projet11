package sid.org.batch;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;







import org.springframework.web.client.RestTemplate;


import sid.org.classe.Pret;




@Component
@EnableScheduling
public class PretBatchServiceImpl implements PretBatchService {

	

	
 @Autowired
private EmailService emailService;
 
 @Autowired
  private TemplateEngine templateEngine;
 private static final Logger logger = LoggerFactory.getLogger(PretBatchServiceImpl.class);

 
 
 @Value("${api.url}")
	private String apiUrl;
	@Value("${spring.api.identifiant}")
	private String identifiant;
	@Value("${spring.api.motDePasse}")
	private String motDePasse;
@Value("${mail.bibliotheque}")
private String biblioMail;
@Value("${mail.subject}")
private String subject;
@Value("${pret.statut3}") 
  private String statut3;	
	

/*
 * creation des différentes variables concernant la singularité du pret 
 * @param locale
 * @param pret
 * @return un objet de type org.thymeleaf.context.Context contenant la date de fin du pret , le livre concerné et le nom de l'utilisateur concerné
 */
public Context variableEmail(final Locale locale,Pret pret) {
	 
	 final Context ctx = new Context(locale);
	
	 ctx.setVariable("dateDeFin", pret.getDateDeFin());
	 ctx.setVariable("livre", pret.getLivre().getNom());
	 ctx.setVariable("user", pret.getUtilisateur().getNom());
	return ctx;
	 
}

/*
 * creer un string representant le html Content du mail
 * @param pret 
 * @param locale
 * @return  String decrivant les infos du context (caracteristiques specifique du pret ) et le html qui represente le squelette du pret
 */
public String createHtmlContent(Pret pret,Locale locale) {
	Context ctx=variableEmail(locale, pret);
	
	
	return templateEngine.process("email.html", ctx);
}



 /**
  * fonction qui envoie un mail a tout les utilisateur possedant un pret dont le statut est depasse
  * @param locale
  *
  */
	
	  
	  @Override 
	  public void envoieMails(Locale locale) throws MessagingException, IOException {
	  
	  
	  RestTemplate rt = new RestTemplate(); final String uri = apiUrl +
	  "/listprets?statut=depasse";
	  
	  
	  
	  HttpHeaders headers = new HttpHeaders();
	  headers.setContentType(MediaType.APPLICATION_JSON);
	  headers.setBasicAuth(identifiant, motDePasse);
	  ParameterizedTypeReference<List<Pret>> responseType = new
	  ParameterizedTypeReference<List<Pret>>() { }; ResponseEntity<List<Pret>> pret
	  = rt.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), responseType);
	  List<Pret>prets= pret.getBody();
	  
	  if (!prets.isEmpty()) { for(int i = 0; i < prets.size(); i++) {
	  
	  String htmlContent = createHtmlContent(prets.get(i), locale);
	  emailService.sendMail(biblioMail,prets.get(i).getUtilisateur().getMail(),
	  subject, htmlContent, locale);
	  
	  
	  }
	  }else { 
		  logger.info("la liste est vide pour les envoies des mails"); }
	  
	  
	  }
	 
	 
/**
 * methode modifiant le statut des prets en fonction de la date de fin du pret 
 * 
 */


	
	@Override
	public void modifierStatutPrets() {
		RestTemplate rt = new RestTemplate();
		final String uri = apiUrl + "/prets/statuts";



		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBasicAuth(identifiant, motDePasse);
		rt.exchange(uri, HttpMethod.PUT, new HttpEntity<>( headers), Long.class);

	}	
	
	
	/**
	 * Methode appelant modifierStatutPrets() et  envoieMails() de maniere repeté
	 * 
	 */
	
	@Scheduled(fixedRate=5000)
	public void effectuerLesEnvoies() throws Exception {
	
	
		Locale locale = new Locale("fr");
		
		modifierStatutPrets(); 
		
		
		 envoieMails(locale); 
		 
		  
		 
		
	}
	

}
