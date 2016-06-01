package test.entity;

import java.math.BigDecimal;
import java.util.Date;

import modelet.entity.AbstractEntity;
import test.AEnum;

public class Book extends AbstractEntity {

  private String bookName;
  private BigDecimal price;
  private Date createDate;
  private AEnum aEnum;
  
  public AEnum getAEnum() {
    return aEnum;
  }


  public void setAEnum(AEnum aEnum) {
    this.aEnum = aEnum;
  }


  public String getBookName() {
    return bookName;
  }


  public void setBookName(String bookName) {
    this.bookName = bookName;
  }


  public BigDecimal getPrice() {
    return price;
  }


  public void setPrice(BigDecimal price) {
    this.price = price;
  }


  public Date getCreateDate() {
    return createDate;
  }


  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }


  public String getTableName() {
    return "book";
  }

  
}
