package modelet.entity;

import java.io.Serializable;

public class SessionContext implements Serializable {

	private Login login = new DefaultLogin();

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}
	
	
}
