package modelet.model.dataroller;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import modelet.entity.Entity;

import org.apache.commons.beanutils.MethodUtils;


public class EntityDataRoller<E extends Entity> extends DataRoller<E> {

  private Class clazz;
  
  public EntityDataRoller(Class clazz) {
    this.clazz = clazz;
  }
  
  @Override
  protected E processRow(List<Column> columns, ResultSet rst) throws Exception {
    
    E entity = (E) Class.forName(this.clazz.getName()).newInstance();
    List<Method> publicSetterMethods = getPublicSetterMethods(this.clazz);
    for (Column column : columns) {
      Object columnValue = retrieveColumnValue(rst, column);
      if (columnValue == null)
        continue;
      for (Method method : publicSetterMethods) {
        if (method.getName().substring(3).equalsIgnoreCase(column.getName())) {
          MethodUtils.invokeMethod(entity, method.getName(), columnValue);
          break;
        }
      }
    }
    return entity;
  }
  
  private List<Method> getPublicSetterMethods(Class clazz) {

    return getPublicMethods(clazz, "set");
  }

  private List<Method> getPublicGetterMethods(Class clazz) {

    return getPublicMethods(clazz, "get");
  }

  private List<Method> getPublicMethods(Class clazz, String getterSetter) {

    List<Method> publicMethods = new ArrayList<Method>();
    Method[] methods = clazz.getMethods();
    for (int i = 0; i < methods.length; i++) {
      Method method = methods[i];
      if (Modifier.isPublic(method.getModifiers()) && method.getName().startsWith(getterSetter))
        publicMethods.add(method);
    }

    return publicMethods;
  }


}
