package modelet.model.dataroller;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import modelet.model.paging.PageContainer;
import modelet.model.paging.PagingElement;

public abstract class DataRoller<E> {
  
  private List<E> entities = new ArrayList<E>();
  private int totalPages = 0;
  private PageContainer<E> pageContainer = null;
  
  public DataRoller() {
  }
  
  public DataRoller(PageContainer<E> pageContainer) {
    this.pageContainer = pageContainer;
  }
  
  public E rollSingleRow(ResultSet rst) {
    
    E entity = null;
    try {
      List<Column> columns = fetchColumnNames(rst);
      entity = processRow(columns, rst);
    } catch (Exception e) {
      exceptionHandler(e);
    }
    return entity;
  }
  
  public void roll(ResultSet rst) {
    
    try {
      List<Column> columns = fetchColumnNames(rst);
      while (rst.next()) {
        E entity = processRow(columns, rst);
        entities.add(entity);
      }
    } catch (Exception e) {
      exceptionHandler(e);
    }
  }
  
  /**
   * 系統會依據指定的頁數來取得結果，但如果指定的頁數大於總頁數，則會回傳第一頁，同時把PagingElement的指定頁數設回第一頁
   */
  public void roll(ResultSet rst, PagingElement pagingElement) {
    
    int page = pagingElement.getTargetPage();
    int rowsPerPage = pagingElement.getRowsPerPage();
    
    int startRow = (page-1) * rowsPerPage; //0 base
    int endRow = startRow + rowsPerPage - 1;
    
    List<E> firstPage = new ArrayList<E>();
    
    try {
      int i = 0;
      List<Column> columns = fetchColumnNames(rst);
      while (rst.next()) {
        //process desired page
        if (i >= startRow && i <= endRow) {
          E entity = processRow(columns, rst);
          entities.add(entity);
        }
        //process first page
        if (i >= 0 && i <= (rowsPerPage-1) && startRow != 0) {
          E entity = processRow(columns, rst);
          firstPage.add(entity);
        }
        i++;
      }
      
      if (pageContainer != null) {
        int totalPages = calculateTotalPages(i, rowsPerPage);
        pageContainer.setTotalPages(totalPages);
        pageContainer.setRows(entities);
        pageContainer.setTotalRecords(i);
      }
      
    } catch (Exception e) {
      exceptionHandler(e);
    }
    
    if (getEntities().size() == 0 && pageContainer != null && page > pageContainer.getTotalPages()) {
      entities = firstPage;
      pagingElement.setTargetPage(1);
    }
  }
  
  /**
   * Please use roll(ResultSet rst, PagingElement pagingElement), its implementation will move as well.
   */
  @Deprecated
  public void roll(ResultSet rst, int page, int rowsPerPage) {
    
    int startRow = (page-1) * rowsPerPage; //0 base
    int endRow = startRow + rowsPerPage - 1;
    
    try {
      int i = 0;
      List<Column> columns = fetchColumnNames(rst);
      while (rst.next()) {
        if (i >= startRow && i <= endRow) {
          E entity = processRow(columns, rst);
          entities.add(entity);
        }
        i++;
      }
      
      if (pageContainer != null) {
        int totalPages = calculateTotalPages(i, rowsPerPage);
        pageContainer.setTotalPages(totalPages);
        pageContainer.setRows(entities);
        pageContainer.setTotalRecords(i);
      }
      
    } catch (Exception e) {
      exceptionHandler(e);
    }
  }
  
  
  
  private int calculateTotalPages(int totalRows, int rowsPerPage) {
    
    int totalPages = totalRows / rowsPerPage;
    if ((totalRows % rowsPerPage) > 0)
      totalPages++;
    return totalPages;
  }
  
  public List<E> getEntities() {
    return entities;
  }
  
  public int getTotalPages() {
    return totalPages;
  }

  protected Object retrieveColumnValue(ResultSet rst, Column column) throws SQLException {
    
    String columnName = column.getName();
    int columnType = column.getType();
    Object columnValue = null;
    if (columnType == Types.DOUBLE || columnType == Types.DECIMAL || columnType == Types.NUMERIC || columnType == Types.FLOAT)
      columnValue = rst.getBigDecimal(columnName);
    else if (columnType == Types.BIT || columnType == Types.TINYINT)
      columnValue = rst.getBoolean(columnName);
    else
      columnValue = rst.getObject(columnName);
    
    return columnValue;
  }
  
  
  private List<Column> fetchColumnNames(ResultSet rst) throws SQLException {
    
    List<Column> columnNames = new ArrayList<Column>();
    ResultSetMetaData metadata = rst.getMetaData();
    for (int i=1; i<=metadata.getColumnCount(); i++) {
      Column column = new Column();
      column.setName(metadata.getColumnLabel(i)); //since MySQL JDBC driver won't return alias name while using getColumnName, so change to getColumnLabel
      column.setType(metadata.getColumnType(i));
      columnNames.add(column);
    }
    return columnNames;
  }
  
  private void exceptionHandler(Exception e) {
    
    e.printStackTrace();
    throw new RuntimeException("Error occured while converting ResultSet to Entity", e);
  }
  
  public static class Column {
    
    private String name;
    private int type;
    public String getName() {
      return name;
    }
    public void setName(String name) {
      this.name = name;
    }
    public int getType() {
      return type;
    }
    public void setType(int type) {
      this.type = type;
    }
  }

  protected abstract E processRow(List<Column> columns, ResultSet rst) throws Exception;
}