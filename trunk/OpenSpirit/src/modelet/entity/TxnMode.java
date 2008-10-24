package modelet.entity;

public class TxnMode {

	public static final TxnMode INSERT = new TxnMode("insert");
  public static final TxnMode UPDATE = new TxnMode("update");
  public static final TxnMode DELETE = new TxnMode("delete");
  public static final TxnMode SELECT = new TxnMode("select");
  
  private String mode;
  
  public TxnMode(String mode) {
  	this.mode = mode;
  }
}
