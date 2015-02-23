package modelet.context;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * This object should not be in HTTP session scope
 * @author matt
 *
 */
@Deprecated
@Component("defaultSessionContext")
@Scope("prototype")
public class DefaultSessionContext implements SessionContext {

  private Login login = new DefaultLogin();
  
  public Login getLogin() {
    return this.login;
  }

  public void setLogin(Login login) {
    this.login = login;
  }

}
