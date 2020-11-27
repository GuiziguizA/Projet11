package sid.org.service;

import org.springframework.data.domain.Page;

import sid.org.classe.Livre;
import sid.org.dto.LivreDto;
import sid.org.exception.BadException;
import sid.org.exception.EntityAlreadyExistException;
import sid.org.exception.ResultNotFoundException;
import sid.org.specification.LivreCriteria;

public interface LivreService {

	public Livre createLivre(LivreDto livre) throws EntityAlreadyExistException, BadException;

	public Livre afficheUnLivre(Long id) throws ResultNotFoundException;

	public Page<Livre> searchLivres(LivreCriteria livreCriteria, int page, int size) throws ResultNotFoundException;

	public void supprimerLivre(Long id) throws ResultNotFoundException;

	public LivreCriteria renvoieUnLivreCriteria();

}
