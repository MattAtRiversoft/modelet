package modelet.entity;

import java.util.List;

public interface Entity {

	public Object getId();
	public void setId(Object id);
	
	public TxnMode getTxnMode();
	public void setTxnMode(TxnMode txnMode);

	public String getTableName();
	public List<String> getKeyNames();
	public List<String> getExclusiveFields();
	
	public void beforeSave();
	public void afterSave();
}
