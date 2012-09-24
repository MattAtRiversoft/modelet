package modelet.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEntity implements Entity, Serializable {

	private Object id;
	private TxnMode txnMode = TxnMode.INSERT;
	private boolean allowNullValue = false;
	
	public List<String> getExclusiveFields() {
	
		List<String> fields = new ArrayList<String>();
		fields.add("id");
		fields.add("txnMode");
		fields.add("allowNullValue");
		
		return fields;
	}
	
	public List<String> getKeyNames() {
		
		List<String> fields = new ArrayList<String>();
		fields.add("id");
		return fields;
	}

	public Object getId() {
		return this.id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public TxnMode getTxnMode() {
		return this.txnMode;
	}
	
	public void setTxnMode(TxnMode txnMode) {
		this.txnMode = txnMode;
	}
	
	public void afterSave() {
  }

  public void beforeSave() {
  }
  
  @Override
  public boolean equals(Object obj) {
    
    if (obj == null)
      return false;
    
    if (!(obj instanceof Entity))
      return false;
    
    AbstractEntity src = (AbstractEntity) obj;
    Object srcId = src.getId();
    
    if (this.id != null && srcId != null)
      return this.id.equals(srcId);
    else
      return false;
  }

  @Override
  public String toString() {
    return "id=" + this.id + ", txnMode=" + this.txnMode.toString();
  }

  public boolean isAllowNullValue() {
    return this.allowNullValue;
  }
  
  public void setAllowNullValue(boolean allow) {
    this.allowNullValue = allow;
  }
  
  public boolean isNew() {
    
    boolean isNew = false;
    if (getId() == null)
      isNew = true;
    
    return isNew;
  }

  public boolean isUpdate() {
    return !isNew();
  }
}
