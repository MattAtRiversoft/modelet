package util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public final class ApplicationContextHelper {

  private static String APPLICATION_CONTEXT = "file:config/spring/core.xml";

  private static AbstractApplicationContext applicationContext;
  
  public static ApplicationContext getApplicationContext() {
    return getApplicationContext(true);
  }
  
  public static ApplicationContext getApplicationContext(boolean initMockSession) {

    if (applicationContext == null) {
      ConfigurableResourceBaseApplicationContext.setResourceBase("/");
      applicationContext = new ConfigurableResourceBaseApplicationContext(APPLICATION_CONTEXT);
      applicationContext.registerShutdownHook();
      if (initMockSession)
        ((ConfigurableResourceBaseApplicationContext)applicationContext).getBeanFactory().registerScope("session", new MockSessionScope());
    }
    return applicationContext;
  }

  public ApplicationContextHelper() {
    // empty
  }

  @SuppressWarnings("unchecked")
  public static <T> T getBean(String id) {
    return (T) getApplicationContext().getBean(id);
  }
}