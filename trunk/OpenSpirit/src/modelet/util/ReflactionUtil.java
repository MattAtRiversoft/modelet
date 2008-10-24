package modelet.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;


public class ReflactionUtil {
  
  /**
   * 把一個class 中的property, 排除static與final 的部分, 通通加到list中
   * @param fieldsCabinet is a list of Field
   * @param clazz is a 
   */
  public static void retrieveFields(List fieldsCabinet, Class clazz) {
    
    Class superClass = clazz.getSuperclass();
    if (superClass != null)
      retrieveFields(fieldsCabinet, superClass);
    
    Field[] fields = clazz.getDeclaredFields();
    for (int i=0; i<fields.length; i++){
      Field field = (Field) fields[i];
      
      int modifier = field.getModifiers();
      if (!Modifier.isStatic(modifier) && !Modifier.isFinal(modifier))
        fieldsCabinet.add(field);
    }
  }
  
//  public static void main(String[] args) {
//    
//    List cabinet = new ArrayList();
//    retrieveFields(cabinet, QueRank.class);
//    
//    System.out.println(cabinet);
//  }  
}
