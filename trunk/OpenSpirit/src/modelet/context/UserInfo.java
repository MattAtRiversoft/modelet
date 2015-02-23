package modelet.context;


public class UserInfo implements Login {

  private String loginId;
  
  public UserInfo(String loginId) {
    this.loginId = loginId;
  }
  
  public String getLoginId() {
    return this.loginId;
  }

  public void setLoginId(String loginId) {
    throw new RuntimeException("You are not authorized to modify user login info");
  }

}
