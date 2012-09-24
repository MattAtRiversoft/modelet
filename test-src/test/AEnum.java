package test;
import java.io.Serializable;



public enum AEnum implements RegulatedType {
  A(100),B(200),C(300);
  
  private int e;
  
  AEnum(int e) {
    this.e = e;
  }

  public String getValue() {
    return String.valueOf(this.e);
  }

  public AEnum newInstanceByBalue(String value) {
    
    AEnum target = null;
    AEnum[] values = values();
    for (int i = 0; i < values.length; i++) {
      AEnum enum1 = values[i];
      if(enum1.getValue().equalsIgnoreCase(value)) {
        target = enum1;
      }
      
    }
    return target;
  }
  
}
