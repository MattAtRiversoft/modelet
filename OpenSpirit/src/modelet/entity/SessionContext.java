package modelet.entity;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("sessionContext")
@Scope("session")
public class SessionContext implements Serializable {

	private Login login = new DefaultLogin();

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}
	
	
}
