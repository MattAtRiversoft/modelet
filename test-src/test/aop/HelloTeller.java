package test.aop;

import org.springframework.stereotype.Component;


@Component("helloTeller")
public class HelloTeller implements IHello {

  public void hello(String name) {
    System.out.println("Teller:Hello " + name);
  }

  public void say(String greeting) {
    System.out.println("Teller:Say " + greeting);
  }

}
