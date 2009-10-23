package modelet.context;


public class DefaultSessionContext implements SessionContext {

  private Login login = new DefaultLogin();
  
  public Login getLogin() {
    return this.login;
  }

  public void setLogin(Login login) {
    this.login = login;
  }

}
