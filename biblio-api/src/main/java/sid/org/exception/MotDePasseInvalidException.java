package sid.org.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.FORBIDDEN, reason="Le mot de passe est invalide")
public class MotDePasseInvalidException  extends BibliothequeException {

	public MotDePasseInvalidException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MotDePasseInvalidException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
