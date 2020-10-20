package sid.org.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Aucun resultat")
public class ResultNotFoundException extends BibliothequeException{

	public ResultNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResultNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	

}
