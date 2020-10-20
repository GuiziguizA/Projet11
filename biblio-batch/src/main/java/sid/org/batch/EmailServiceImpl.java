package sid.org.batch;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;




import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


import org.springframework.stereotype.Service;




import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.thymeleaf.TemplateEngine;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class EmailServiceImpl implements EmailService  {


	 @Autowired
	    private JavaMailSender emailSender;

	 
	 
	
	
		
		@Value("${mail.bibliotheque}")
		private String biblioMail;
		@Value("${mail.subject}")
		private String subject;
		@Value("${pret.statut3}") 
		  private String statut3;	

		 private static final Logger logger = LoggerFactory.getLogger( EmailServiceImpl.class);
		 
		 
	  @Override 
	  public void sendMail(String from, String to, String subject,String htmlContent,Locale locale) throws MessagingException, IOException{
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
	 
	  
	  
	
	
		
}