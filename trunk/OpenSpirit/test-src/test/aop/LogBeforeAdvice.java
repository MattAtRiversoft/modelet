package test.aop;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.stereotype.Component;

@Component("logBeforeAdvice")
public class LogBeforeAdvice implements MethodBeforeAdvice {

  public void before(Method method, Object[] args, Object target) throws Throwable {
    System.out.println("be going to call " + method.getName() + ", params=[" + args.toString() + "], " + target.getClass().getName());
  }
}
