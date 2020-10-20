package sid.org.config;



  

  import org.springframework.beans.factory.annotation.Autowired; 
  import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

  import org.springframework.security.config.annotation.authentication.builders.
  AuthenticationManagerBuilder; 
  import org.springframework.security.config.annotation.method.configuration.
  EnableGlobalMethodSecurity; 
  import org.springframework.security.config.annotation.web.builders.HttpSecurity;
  import org.springframework.security.config.annotation.web.configuration.
  EnableWebSecurity;
  import org.springframework.security.config.annotation.web.configuration.
  WebSecurityConfigurerAdapter;
  import org.springframework.security.web.AuthenticationEntryPoint; 
 
 
/**
 * 
 * @author guali
 *fonction spring security initialisant la basic authentification
 */
  @Configuration
  
  @EnableWebSecurity
  
  @EnableGlobalMethodSecurity(securedEnabled = true) public class
  ApiSecurityConfig extends WebSecurityConfigurerAdapter {
  
 /**
	 * configuration de spring security
	 * 
	 * 
	 * 
	 */
		  @Autowired 
		  CustomUserDetailService userDetailsService;
		  
		  @Autowired 
		  private AuthenticationEntryPoint authEntryPoint;
		  
		  @Override protected void configure(AuthenticationManagerBuilder auth) throws
		  Exception { auth.userDetailsService(userDetailsService); }
		  
		  @Override protected void configure(HttpSecurity http) throws Exception { http
		  .csrf().disable();
		  
		  http.authorizeRequests()
		.antMatchers(HttpMethod.POST,"/users/identity").permitAll()
		.antMatchers(HttpMethod.POST,"/users").permitAll()
		.and()
        .authorizeRequests().anyRequest().authenticated();
		  
		  
		  http.httpBasic().authenticationEntryPoint(authEntryPoint);
		  
		  
		  }
		  
		  
		  
		  
		  }
		 
