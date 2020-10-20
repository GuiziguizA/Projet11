package sid.org.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import sid.org.classe.Livre;

public interface  LivreRepository extends JpaRepository<Livre, Long>, JpaSpecificationExecutor<Livre> {
	
	public Optional<Livre>findByCodeLivre(Long id);
	
	@Query("SELECT c FROM Livre c WHERE  (:nom is null"+ " or c.nom = :nom)"
			+"and (:type is null"+ " or c.type = :type)"	
			)
	public ArrayList<Livre> findByNomAndtype(@Param("nom")String nom,@Param("type")String type);
	
	public List<Livre>findByNom(String nom);
	public List<Livre>findByAuteur(String auteur);
	
	public List<Livre>findByAuteurAndNom(String auteur,String nom);
}
