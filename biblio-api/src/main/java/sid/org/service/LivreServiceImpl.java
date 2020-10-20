package sid.org.service;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import sid.org.classe.Livre;
import sid.org.dao.LivreRepository;
import sid.org.dto.LivreDto;
import sid.org.exception.BibliothequeException;

import sid.org.exception.EntityAlreadyExistException;

import sid.org.exception.LivreIndisponibleException;
import sid.org.exception.ResultNotFoundException;
import sid.org.specification.LivreCriteria;
import sid.org.specification.LivreSpecificationImpl;





@Component
public class LivreServiceImpl implements LivreService{
	@Autowired
	private LivreRepository livreRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(LivreServiceImpl.class);
	

	/*
	 * Creation d'un Livre
	 * @param livreDto 
	 * 
	 * @return Livre
	 */
	
	@Override
	public Livre createLivre( LivreDto livreDto) throws EntityAlreadyExistException {
		
		List<Livre>livreAuteurAndNom=livreRepository.findByAuteurAndNom(livreDto.getAuteur(),livreDto.getNom());
		if(!livreAuteurAndNom.isEmpty()  ) {
			throw new  EntityAlreadyExistException("Ce livre existe deja");
			
		}
		
		Livre livre= convertoToEntity(livreDto);
		logger.info("le livre a ete ajouté");
		return 	livreRepository.save(livre);
		
	}

	/*
	 * Suppression d'un Livre
	 * @param Long id  
	 * 
	 * 
	 */
@Override
	public void supprimerLivre(Long id)throws ResultNotFoundException{
		
		Optional<Livre>livre=livreRepository.findById(id);
		if(!livre.isPresent()) {
			throw new ResultNotFoundException();
		}
		livreRepository.delete(livre.get());
	}
	
/*
 *Affichage  d'un Livre
 * @param L 
 * 
 * @return Livre
 */
	
	@Override
	public Livre  afficheUnLivre( Long id) throws ResultNotFoundException{
		Optional<Livre> book=livreRepository.findByCodeLivre(id);
		if(!book.isPresent()) {
			throw new ResultNotFoundException("le livre existe pas");
		}
		
		 
	
			return book.get();
		}


	/*
	 *Rechercher des livre en fonction d'un critere
	 * @param  livreCriteria 
	 * @param  page 
	 * @param  size
	 * @return une page contenant des livres en fonction du livreCriteria
	 */


@Override
public Page<Livre> searchLivres(LivreCriteria livreCriteria,int page ,int size) throws ResultNotFoundException {
	LivreSpecificationImpl spec = new LivreSpecificationImpl(livreCriteria);
	
	if(size==0) {
		throw new ResultNotFoundException();
	}
	
	Pageable pageable=PageRequest.of(page,size );
	
	Page<Livre> results = livreRepository.findAll(spec,pageable);
	
	
	return results;
}


/*
 *Convertir un LivreDto en Livre
 * @param  livreDto 
 * 
 * 
 * @return Livre
 */

private Livre convertoToEntity(LivreDto livreDto) {
	
	Livre livre=new Livre();
	
	livre.setAuteur(livreDto.getAuteur());
	livre.setEmplacement(livreDto.getEmplacement());
	livre.setNom(livreDto.getNom());
	livre.setNombreExemplaire(livreDto.getNombreExemplaire());
	livre.setSection(livreDto.getSection());
	livre.setType(livreDto.getType());
	return livre;
	
}

}
