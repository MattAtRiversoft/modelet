package modelet.entity;

import java.util.Locale;

public class DefaultLogin implements Login {

	private String login;
	
	public Locale getLocale() {
		return Locale.getDefault();
	}

	public String getLoginId() {
		return this.login;
	}

	public void setLoginId(String loginId) {
		this.login = loginId;
	}

}
