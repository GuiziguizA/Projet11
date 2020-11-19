package sid.org.batch;

import java.io.IOException;
import java.util.Locale;

import javax.mail.MessagingException;

import sid.org.classe.Pret;

public interface PretBatchService {

	public void envoieMails(Locale locale) throws MessagingException, IOException;

	public void modifierStatutPrets();

	public String createHtmlContent(Pret pret, Locale locale);

	public void TimerDisponibiliteLivre(Long idPret);

	public void connectPretEnAttente(Long id);

}
