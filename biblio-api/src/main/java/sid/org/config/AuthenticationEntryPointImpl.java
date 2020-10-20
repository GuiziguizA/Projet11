package sid.org.config;



  import java.io.IOException; import java.io.PrintWriter;
  
  
  import javax.servlet.http.HttpServletRequest; import
  javax.servlet.http.HttpServletResponse;
  
  import org.springframework.security.core.AuthenticationException; import
  org.springframework.security.web.authentication.www.
  BasicAuthenticationEntryPoint; import
  org.springframework.stereotype.Component;
  /**
   * 
   * @author guali
   *genère une fenetre pour réalisé la basicAthentification
   */
  @Component
  public class AuthenticationEntryPointImpl extends BasicAuthenticationEntryPoint {
   
      @Override
      public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
              throws IOException {
          response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName());
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          PrintWriter writer = response.getWriter();
          writer.println("HTTP Status 401 - " + authEx.getMessage());
      }
   
      @Override
      public void afterPropertiesSet() {
          // RealmName appears in the login window (Firefox).
          setRealmName("o7planning");
          super.afterPropertiesSet();
      }
  }