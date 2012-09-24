package test.aop;

import org.springframework.stereotype.Component;


@Component("helloSpeaker")
public class HelloSpeaker implements IHello {

  public void hello(String name) {
    System.out.println("Hello " + name);
  }

  public void say(String greeting) {
    System.out.println("Say " + greeting);
  }

}
