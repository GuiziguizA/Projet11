package sid.org.biblio.front.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	


	/**
	 * configuration de spring security
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {


		http.addFilterBefore(new TwoFactorAuthenticationFilter((authenticationManagerBean())), UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests().antMatchers("/css/**", "/js/**", "/webjars/**", "/user/form").permitAll()
				.antMatchers(HttpMethod.GET, "/users/form").permitAll().antMatchers(HttpMethod.POST, "/users")
				.permitAll().anyRequest().authenticated().and()
				.formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/home").and().logout()
				.logoutSuccessUrl("/login");

	}
	 
}