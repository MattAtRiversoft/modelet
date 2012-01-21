package test.aop;

import modelet.model.Model;
import modelet.model.paging.PagingElement;

import org.junit.Test;

import util.ApplicationContextHelper;


public class TestAop {

  @Test
  public void test1() {
    
    IHello ihello = ApplicationContextHelper.getBean("helloTeller");
    ihello.hello("Matt");
    ihello.say("good night");
    
    ihello = ApplicationContextHelper.getBean("helloSpeaker");
    ihello.hello("Matt");
    ihello.say("good night");
    
  }
  
  @Test
  public void test2() {
    
    Model model = ApplicationContextHelper.getBean("modelFindBeforeProxy");
    PagingElement pagingElement = ApplicationContextHelper.getBean("pagingElement");
    model.find("select * from sysusers", null);
    model.findWithPaging("select * from sysusers", null, pagingElement);
  }
}
