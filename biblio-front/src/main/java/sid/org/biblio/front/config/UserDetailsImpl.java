package sid.org.biblio.front.config;


import javax.servlet.http.HttpSession;

import org.apache.catalina.startup.PasswdUserDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import sid.org.biblio.front.classe.Sessions;
import sid.org.biblio.front.classe.Utilisateur;



@Service
public class UserDetailsImpl implements UserDetailsService{
	
	
	
	
	@Value("${api.url}")
	private String apiUrl;
	
	
	/**
	 * Modifie le User details de spring data security en identifiant l'utilisateur par son mail dans BD
	 * @param mail
	 * @return User.withUsername(utilisateur.get().getMail())
        .password( utilisateur.get().getPassword())
        .roles(utilisateur.get().getRole().getNom()).build();
	 */
	@Override
	public UserDetails loadUserByUsername(String mailAndPassword)  {
		
		RestTemplate rt = new RestTemplate();
		final String uri = apiUrl + "/users/identity";
		
		
		
		 String[] split =mailAndPassword.split(":");
		    String username = split[0];
		    String motDePasse = split[1];
		   
		    Sessions sessions=new Sessions();
		    sessions.setMail(username);
		    sessions.setMotDePasse(motDePasse);
			ResponseEntity<Utilisateur> user= rt.exchange(uri,HttpMethod.POST,new HttpEntity<>(sessions),Utilisateur.class);
			Utilisateur utilisateur = user.getBody();
		
	if(utilisateur==null) {
		throw new UsernameNotFoundException("l'utilisateur n'existe pas");
	}
		
	

		return  User.withUsername(utilisateur.getMail())
        .password( utilisateur.getMotDePasse())
        .roles(utilisateur.getRoles().getNom()).build();
	}






}
