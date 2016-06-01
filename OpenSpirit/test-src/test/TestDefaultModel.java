package test;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;

import modelet.model.DefaultModel;
import test.entity.Book;
import util.ApplicationContextHelper;

public class TestDefaultModel {

  @Test
  public void testPersist() {
    
    Book book = new Book();
    book.setBookName("賈'伯'斯'傳");
    book.setCreateDate(new Date());
    book.setPrice(new BigDecimal("100"));
    book.setAEnum(AEnum.A);
    
    DefaultModel model = ApplicationContextHelper.getBean("defaultModel");
    model.save(book);
  }
}
