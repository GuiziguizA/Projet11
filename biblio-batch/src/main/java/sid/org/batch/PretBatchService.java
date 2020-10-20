package sid.org.batch;

import java.io.IOException;
import java.util.Locale;

import javax.mail.MessagingException;

import sid.org.classe.Pret;


public interface PretBatchService {

	
	  void envoieMails(Locale locale) throws MessagingException, IOException;
	 
	void modifierStatutPrets();

	String createHtmlContent(Pret pret, Locale locale);

}
