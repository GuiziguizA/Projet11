package sid.org.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import sid.org.exception.ResultNotFoundException;
@Component
public class DateServiceImpl implements DateService{

	/*
	 * modification d'une date
	 * @param date
	 * @param jours
	 * @return une date modifier du nombre de jour ajouté
	 */
	
	
	@Override
	public Date modifierDate(Date date,int jours) throws ResultNotFoundException {
		
		
		  Calendar c = Calendar.getInstance();
	        c.setTime(date);
	        c.add(Calendar.DATE, jours);
	        Date after = c.getTime();
		return after;

	}
	

	
	
}
