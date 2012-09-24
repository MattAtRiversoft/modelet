package modelet.model;


public class TransactionRollbackedException extends ModelException {

	private static final long serialVersionUID = -2577309222073903595L;

	public TransactionRollbackedException(String message) {
		
		this(message, null, null);
	}
	
	public TransactionRollbackedException(String message, String internalMessage) {
		
		this(message, internalMessage, null);
	}
	
	public TransactionRollbackedException(String message, Throwable cause) {
		
		this(message, null, cause);
	}
	
	public TransactionRollbackedException(String message, String internalMessage, Throwable cause) {
		
		super(message, internalMessage, cause);
//		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	}
}
