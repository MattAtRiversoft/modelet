package modelet.model.dataroller;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import modelet.entity.Entity;
import modelet.model.paging.PageContainer;

import org.apache.commons.beanutils.MethodUtils;


public class EntityDataRoller<E extends Entity> extends DataRoller<E> {

  private Class clazz;
  
  public EntityDataRoller(Class clazz) {
    this.clazz = clazz;
  }
  
  public EntityDataRoller(Class clazz, PageContainer<E> pageContainer) {
    
    super(pageContainer);
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
          if (isSetterParamAEnum(method)) {
            Class[] clazzs =  method.getParameterTypes();
            columnValue = Enum.valueOf(clazzs[0], columnValue.toString());
          }
          MethodUtils.invokeMethod(entity, method.getName(), columnValue);
          break;
        }
      }
    }
    return entity;
  }
  
  private boolean isSetterParamAEnum(Method method) {
    
    Class[] clazzs =  method.getParameterTypes();
    if (clazzs.length != 1) {
      throw new RuntimeException("method setter should have at least one paramter.");
    }
    return clazzs[0].isEnum();
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
