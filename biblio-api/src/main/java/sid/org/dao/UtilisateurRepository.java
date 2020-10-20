package sid.org.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import sid.org.classe.Utilisateur;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
	
	public Optional<Utilisateur> findByMail(String mail);
	

}
