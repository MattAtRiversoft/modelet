package modelet.model.dataroller;

import java.sql.ResultSet;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;


public class MapDataRoller extends DataRoller<SortedMap<String, Object>> {

  @Override
  protected SortedMap<String, Object> processRow(List<Column> columns, ResultSet rst) throws Exception {
    
    SortedMap<String, Object> map = new TreeMap<String, Object>();
    for (Column column : columns) {
      Object columnValue = retrieveColumnValue(rst, column);
      map.put(column.getName(), columnValue);
    }
    return map;
  }

}
