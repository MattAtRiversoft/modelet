package modelet.entity;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEntity implements Entity {

	private Object id;
	private TxnMode txnMode = TxnMode.INSERT;
	
	
	public List<String> getExclusiveFields() {
	
		List<String> fields = new ArrayList<String>();
		fields.add("id");
		fields.add("txnMode");
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

}
