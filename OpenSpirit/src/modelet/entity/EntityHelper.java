package modelet.entity;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelet.util.ReflactionUtil;

import org.apache.commons.beanutils.MethodUtils;


public class EntityHelper {

	/**
   * 把這個entity 的property name和property value 塞到Map 中, (包含所有 super class的property), 以供組成SQL statement
   */
  public static Map<String, Object> convert(Entity entity) {
    
    Map<String, Object> value = new HashMap<String, Object>();
    
    List fields = new ArrayList();
    ReflactionUtil.retrieveFields(fields, entity.getClass());
    
    for (int i=0; i<fields.size(); i++) {
      Field field = (Field) fields.get(i);
      
      String fieldName = field.getName();
      if (entity.getExclusiveFields().contains(fieldName))
        continue;

      String prefix = "get";
      if (field.getType() == boolean.class)
        prefix = "is";
      
      String methodName = prefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
      try {
        Object fieldValue = MethodUtils.invokeMethod(entity, methodName, null);
        if (fieldValue instanceof String) {
          fieldValue = ((String)fieldValue).replaceAll("'", "''");
          //fieldValue = "'" + fieldValue + "'"; because prepared statement used, no need to add field value
        }
        else if ((fieldValue instanceof Calendar)) {
        	fieldValue = new Timestamp(((Calendar)fieldValue).getTimeInMillis());
        }
        else if ((fieldValue instanceof Date)) {
        	fieldValue = new Timestamp(((Date)fieldValue).getTime());
        }
        else if (fieldValue instanceof Boolean) {
          if (((Boolean)fieldValue).booleanValue())
            fieldValue = "1"; //true
          else
            fieldValue = "0"; //false
        }
        
        //要允許把table 欄位資料ull, 因為有可能就是要把值設為null
        //if (fieldValue != null)
          value.put(fieldName, fieldValue);
      }
      catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    }
    return value;
  }
}
