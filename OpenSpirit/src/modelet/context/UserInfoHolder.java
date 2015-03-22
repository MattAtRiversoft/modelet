package modelet.context;

/**
 * This UserInfoHolder is built to replace UserContext
 * @author matt
 *
 */
public class UserInfoHolder {
  
  private static final ThreadLocal<Login> meHolder = new ThreadLocal<Login>();
  
  public static void put(Login util) {
    meHolder.set(util);
  }
  
  public static Login get() {
    return meHolder.get();
  }
}
