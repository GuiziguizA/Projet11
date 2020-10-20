package sid.org.batch;



import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import sid.org.batch.PretBatchService;



@Component
public class EnvoiDesMails {
	@Autowired
	PretBatchService pretBatchService;
	
	public void effectuerLesEnvoies() throws Exception {
	
	
		Locale locale = new Locale("fr");
		 pretBatchService.modifierStatutPrets(); 
		
		
		  pretBatchService.envoieMails(locale);
		 
		  
		 
		
	}
}
