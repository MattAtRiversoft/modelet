package util;

import org.springframework.beans.BeansException;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.Resource;

/*
 * any Resource type properties that are injected via plain strings without url prefix will have its path prepended with resource base
 * ie: "WEB-INF/web.xml" => "web/WEB-INF/web.xml" if resource base is "web/"
 *     "file://WEB-INF/web.xml" remains unchanged
 */
public class ConfigurableResourceBaseApplicationContext extends FileSystemXmlApplicationContext {
  
  private static String resourceBase = "";
  
  public static void setResourceBase(String resourceBase) {
    
    ConfigurableResourceBaseApplicationContext.resourceBase = resourceBase;
  }
  
  public ConfigurableResourceBaseApplicationContext(String configLocation) throws BeansException {
    
    super(configLocation);
  }
  
  protected Resource getResourceByPath(String path) {

    return super.getResourceByPath(ConfigurableResourceBaseApplicationContext.resourceBase + path);
  }
}
