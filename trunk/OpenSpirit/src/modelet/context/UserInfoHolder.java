package modelet.context;

/**
 * This UserInfoHolder is built to replace UserContext
 * @author matt
 *
 */
public class UserInfoHolder {
  
  private static final ThreadLocal<UserInfo> meHolder = new ThreadLocal<UserInfo>();
  
  public static void put(UserInfo util) {
    meHolder.set(util);
  }
  
  public static UserInfo get() {
    return meHolder.get();
  }
}
