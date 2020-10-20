package sid.org.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Ce livre n'est plus disponible")
public class LivreIndisponibleException extends BibliothequeException{
	public LivreIndisponibleException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LivreIndisponibleException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
}
