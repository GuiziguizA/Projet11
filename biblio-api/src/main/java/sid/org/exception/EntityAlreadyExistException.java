package sid.org.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Cet element existe deja")
public class EntityAlreadyExistException extends BibliothequeException{

	
	public EntityAlreadyExistException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EntityAlreadyExistException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
}
