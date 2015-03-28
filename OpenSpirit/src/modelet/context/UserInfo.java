package modelet.context;


public class UserInfo implements Login {

  private String loginId;
  private Long dbId;
  
  public UserInfo(String loginId, Long dbId) {
    this.loginId = loginId;
    this.dbId = dbId;
  }
  
  public String getLoginId() {
    return this.loginId;
  }
  
  public Long getDbId() {
    return dbId;
  }

  public void setDbId(Long dbId) {
    this.dbId = dbId;
  }

}
