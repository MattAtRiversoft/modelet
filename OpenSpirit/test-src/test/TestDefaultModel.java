package test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import modelet.entity.TxnMode;
import modelet.model.DefaultModel;
import test.entity.Book;
import util.ApplicationContextHelper;

public class TestDefaultModel {

  public static void main(String[] args) {
   
    TestDefaultModel me = new TestDefaultModel();
    me.testPersist();
  }
  
  @Test
  public void testPersist() {
    
    Book book = new Book();
    book.setBookName("Java學習筆記 第四版");
    book.setCreateDate(new Date());
    book.setPrice(new BigDecimal("150"));
    book.setAEnum(AEnum.A);
    
    DefaultModel model = ApplicationContextHelper.getBean("defaultModel");
    model.save(book);
    
    book.setAEnum(AEnum.B);
    
    model.save(book);
    
    book.setTxnMode(TxnMode.DELETE);
    model.save(book);
    
    
    Book myBook = model.getEntityById(4L, "book", Book.class);
    System.out.println(myBook.getBookName());
    
    Object[] params = {1L};
    List<Book> books = model.find("select * from book where id > ? ", params, Book.class);
    for (Book abook : books) {
      System.out.println(abook.getBookName());
    }
    
    List<Map<String, Object>> rows = model.find("select * from book where id > ? ", params);
    for (Map<String, Object> row : rows) {
      System.out.println(row.get("bookName") + "," + row.get("createDate"));
    }
  }
}
