package sid.org.batch;

import java.io.IOException;
import java.util.Locale;

import javax.mail.MessagingException;

import org.springframework.stereotype.Service;



public interface EmailService {
	public void sendMail(String from, String to, String subject, String htmlContent, Locale locale) throws MessagingException, IOException;
}
