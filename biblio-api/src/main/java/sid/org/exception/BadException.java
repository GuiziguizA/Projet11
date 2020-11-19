package sid.org.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "l'element est incorrect")
public class BadException extends BibliothequeException {

	public BadException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BadException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
}
