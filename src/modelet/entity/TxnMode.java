package modelet.entity;

import java.io.Serializable;

public class TxnMode implements Serializable {

  public static final TxnMode INSERT = new TxnMode("insert");
  public static final TxnMode UPDATE = new TxnMode("update");
  public static final TxnMode DELETE = new TxnMode("delete");
  public static final TxnMode SELECT = new TxnMode("select");
  
  private String mode;
  
  public TxnMode() {
    this(INSERT.toString());
  }
  
  public TxnMode(String mode) {
    this.mode = mode;
  }
  
  public String getMode() {
    return mode;
  }
  
  public void setMode(String mode) {
    this.mode = mode;
  }

  public String toString() {
    return this.mode;
  }
  
  @Override
  public boolean equals(Object obj) {
    
    if (obj == null)
      return false;
    
    if (!(obj instanceof TxnMode))
      return false;
    
    TxnMode src = (TxnMode) obj;
    return this.toString().equals(src.toString());
  }
  
  

}
