package sid.org.biblio.front.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

public class SimpleAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	@Autowired
	private PasswordEncoder passwordEncoder;
  @Override
  public Authentication attemptAuthentication(
    HttpServletRequest request, 
    HttpServletResponse response) 
      throws AuthenticationException {

      // ...

      UsernamePasswordAuthenticationToken authRequest
        = getAuthRequest(request);
      setDetails(request, authRequest);
       
      return this.getAuthenticationManager()
        .authenticate(authRequest);
  }

  private UsernamePasswordAuthenticationToken getAuthRequest(
    HttpServletRequest request) {

      String username = obtainUsername(request);
      String password = passwordEncoder.encode(obtainPassword(request));
    

      // ...

      String usernameDomain = String.format("%s%s%s", username.trim(), 
        String.valueOf(Character.LINE_SEPARATOR));
      return new UsernamePasswordAuthenticationToken(
        usernameDomain, password);
  }

  // other methods
}