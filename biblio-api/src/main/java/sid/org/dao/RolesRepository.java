package sid.org.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import sid.org.classe.Roles;

public interface RolesRepository extends JpaRepository<Roles, Long> {

	
	public Optional<Roles> findByNom(String nom);
}
