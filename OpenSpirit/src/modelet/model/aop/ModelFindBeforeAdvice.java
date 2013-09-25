package modelet.model.aop;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.stereotype.Component;

@Component("modelFindBeforeAdvice")
public class ModelFindBeforeAdvice implements MethodBeforeAdvice {

  public void before(Method method, Object[] args, Object target) throws Throwable {
    
//    System.out.print("Target object=" + target.getClass().getName() + ", ");
//    System.out.print("target method=" + method.getName() + ", ");
//    System.out.print("target args=[");
//    if (args != null) {
//      for (Object obj : args) {
//        System.out.print((obj == null)?"null, ":obj.toString() + ", ");
//      }
//    }
//    System.out.print("] \r\n"); 
  }
  
}
