package sid.org.config;


  import java.util.Optional;
  
  import org.springframework.beans.factory.annotation.Autowired; import
  org.springframework.security.core.userdetails.User; import
  org.springframework.security.core.userdetails.UserDetails; import
  org.springframework.security.core.userdetails.UserDetailsService; import
  org.springframework.security.core.userdetails.UsernameNotFoundException;
  
  import org.springframework.stereotype.Service;
  
  import sid.org.classe.Utilisateur; import sid.org.dao.UtilisateurRepository;
 

  @Service
  public class CustomUserDetailService implements UserDetailsService{
  
  
  
  
  @Autowired 
  private UtilisateurRepository utilisateurRepository;
  
  
 /**
	 * Modification du UserDetails de spring Security
	 * 
	 * 
	 * @param mail
	 * @return User.withUsername(utilisateur.get().getMail()) .password(
	 *         utilisateur.get().getPassword())
	 *         .roles(utilisateur.get().getRole().getNom()).build();
	 */
		  @Override public UserDetails loadUserByUsername(String mail) throws
		  UsernameNotFoundException { Optional<Utilisateur>
		  utilisateur=utilisateurRepository.findByMail(mail);
		  
		  if(!utilisateur.isPresent()) { throw new
		  UsernameNotFoundException("l'utilisateurn'existe pas "); }
		  
		  
		  return User.withUsername(utilisateur.get().getMail()) .password(
		  utilisateur.get().getMotDePasse())
		  .roles(utilisateur.get().getRoles().getNom()).build(); }
		  
		  }
		 