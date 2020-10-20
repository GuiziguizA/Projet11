package sid.org.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import sid.org.classe.Livre;

public class LivreSpecificationImpl implements Specification<Livre> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private LivreCriteria criteria;
	
	
	public LivreSpecificationImpl(LivreCriteria criteria) {
		super();
		this.criteria = criteria;
	}
	public LivreSpecificationImpl() {
		super();
		// TODO Auto-generated constructor stub
	}
	/*
	 * Modification de l'objet predicate
	 *@param Root<Livre> root
	 *@param CriteriaQuery<?> query
	 *@param CriteriaBuilder builder
	 *@return Predicate
	 */
	@Override
	public Predicate toPredicate(Root<Livre> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		Predicate predicate = builder.conjunction();
		if (criteria.getCodeLivre()!=null) {
			predicate.getExpressions().add(builder.equal(root.get("codeLivre"),criteria.getCodeLivre()));
		}
		if (criteria.getNom()!=null) {
			predicate.getExpressions().add(builder.like(root.get("nom"),"%"+criteria.getNom()+"%"));
		}
		if (criteria.getAuteur()!=null) {
			predicate.getExpressions().add(builder.like(root.get("auteur"),"%"+criteria.getAuteur()+"%"));
		}
		if (criteria.getType()!=null) {
			predicate.getExpressions().add(builder.like(root.get("type"),"%"+criteria.getType()+"%"));
		}
		return builder.and(predicate);
		
	           
		 
	
	}

	
}