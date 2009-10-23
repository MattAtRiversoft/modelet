package modelet.model.paging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component("pagingElement")
@Scope("prototype")
public class PagingElement {

  private Integer targetPage = new Integer(1);
  
  @Autowired
  private Integer rowsPerPage;

  
  public Integer getTargetPage() {
    return targetPage;
  }
  
  public void setTargetPage(Integer targetPage) {
    this.targetPage = targetPage;
  }
  
  public Integer getRowsPerPage() {
    return rowsPerPage;
  }
  
  public void setRowsPerPage(Integer rowsPerPage) {
    this.rowsPerPage = rowsPerPage;
  }
  
  
}
