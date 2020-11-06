package sid.org.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sid.org.classe.Livre;
import sid.org.classe.Pret;
import sid.org.classe.Utilisateur;

public interface PretRepository extends JpaRepository<Pret, Long> {

	public Page<Pret> findByUtilisateur(Utilisateur utilisateur, Pageable pageable);

	public List<Pret> findByStatut(String statut);

	public List<Pret> findByUtilisateurAndLivre(Utilisateur utilisateur, Livre livre);

	public Optional<Pret> findByUtilisateurAndLivreAndStatut(Utilisateur utilisateur, Livre livre, String statut);

	public List<Pret> findByStatutAndLivre(String statut, Livre livre);

	public List<Pret> findByLivre(Livre livre);

	@Query("select u from Pret u where u.livre = :livre and u.statut != :statut order by dateDeFin")
	public List<Pret> findLivreandStatutNotByOrderByDateDeFinAsc(@Param("livre") Livre livre,
			@Param("statut") String statut);

}
