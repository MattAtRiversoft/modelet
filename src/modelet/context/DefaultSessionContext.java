package modelet.context;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("defaultSessionContext")
@Scope("session")
public class DefaultSessionContext implements SessionContext {

  private Login login = new DefaultLogin();
  
  public Login getLogin() {
    return this.login;
  }

  public void setLogin(Login login) {
    this.login = login;
  }

}
