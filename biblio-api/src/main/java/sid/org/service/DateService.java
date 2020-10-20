package sid.org.service;

import java.util.Date;


import sid.org.exception.ResultNotFoundException;

public interface DateService {
	

	public Date modifierDate(Date date, int jours) throws ResultNotFoundException;

}
