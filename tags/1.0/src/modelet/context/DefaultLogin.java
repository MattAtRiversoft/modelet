package modelet.context;


public class DefaultLogin implements Login {

	private String login = "SYSTEM";
	
//	public Locale getLocale() {
//		return Locale.getDefault();
//	}

	public String getLoginId() {
		return this.login;
	}

	public void setLoginId(String loginId) {
		this.login = loginId;
	}

}
