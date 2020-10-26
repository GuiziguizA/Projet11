package sid.org.service;

import java.io.IOException;
import java.util.Locale;

import javax.mail.MessagingException;

import org.thymeleaf.context.Context;

import sid.org.classe.Pret;

public interface EmailService {
	public void sendMail(String from, String to, String subject, String htmlContent, Locale locale)
			throws MessagingException, IOException;

	public Context variableEmail(Locale locale, Pret pret);

	public String createHtmlContent(Pret pret, Locale locale);
}
