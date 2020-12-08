package sid.org.service;

import java.util.Locale;
import java.util.Optional;

import org.thymeleaf.context.Context;

import sid.org.classe.Livre;

public interface EmailService {
	public void sendMail(String from, String to, String subject, String htmlContent, Locale locale);

	public Context variableEmail(Locale locale, String mail, Livre livre);

	public String createHtmlContent(String mail, Livre livre, Locale locale);

	public void envoyerLeMail(Optional<Livre> livre);

}
