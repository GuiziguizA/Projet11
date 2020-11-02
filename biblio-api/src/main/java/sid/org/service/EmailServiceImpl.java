package sid.org.service;

import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import sid.org.classe.Livre;
import sid.org.dao.UtilisateurRepository;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender emailSender;
	@Autowired
	private TemplateEngine templateEngine;
	@Autowired
	private UtilisateurRepository utilisateurRepository;

	@Value("${mail.bibliotheque}")
	private String biblioMail;
	@Value("${mail.subject}")
	private String subject;
	@Value("${pret.statut3}")
	private String statut3;

	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Override
	public void sendMail(String from, String to, String subject, String htmlContent, Locale locale) {
		try {

			MimeMessage mimeMessage = this.emailSender.createMimeMessage();
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");

			message.setSubject(subject);
			message.setFrom(from);
			message.setTo(to);
			message.setText(htmlContent, true); // true = isHtml

			this.emailSender.send(mimeMessage);
		} catch (MessagingException ex) {
			logger.info(ex.getMessage());
		}

	}

	/*
	 * creation des différentes variables concernant la singularité du pret
	 * 
	 * @param locale
	 * 
	 * @param pret
	 * 
	 * @return un objet de type org.thymeleaf.context.Context contenant la date de
	 * fin du pret , le livre concerné et le nom de l'utilisateur concerné
	 */
	@Override
	public Context variableEmail(final Locale locale, String mail, Livre livre) {

		final Context ctx = new Context(locale);

		ctx.setVariable("livre", livre.getNom());
		ctx.setVariable("user", utilisateurRepository.findByMail(mail).get().getNom());
		return ctx;

	}

	/*
	 * creer un string representant le html Content du mail
	 * 
	 * @param pret
	 * 
	 * @param locale
	 * 
	 * @return String decrivant les infos du context (caracteristiques specifique du
	 * pret ) et le html qui represente le squelette du pret
	 */
	@Override
	public String createHtmlContent(String mail, Livre livre, Locale locale) {
		Context ctx = variableEmail(locale, mail, livre);

		return templateEngine.process("email.html", ctx);
	}

}