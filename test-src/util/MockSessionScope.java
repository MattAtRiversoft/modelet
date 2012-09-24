package util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;


public class MockSessionScope implements Scope {

  private Map<String,Object> scopeMap = Collections.synchronizedMap(new HashMap<String,Object>());

  public Object get(String bean, ObjectFactory factory) {
    
    Object o = scopeMap.get(bean);
    if (o == null) {
      o = factory.getObject();
      scopeMap.put(bean, o);
    }
    return o;
  }

  public String getConversationId() {
    throw new RuntimeException("not implimented");
  }

  public void registerDestructionCallback(String arg0, Runnable arg1) {
    return;
  }

  public Object remove(String bean) {
    return scopeMap.remove(bean);
  }

  public Object resolveContextualObject(String arg0) {
    throw new RuntimeException("Not implement yet.");
  }

}
