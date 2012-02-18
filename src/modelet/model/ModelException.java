package modelet.model;

public class ModelException extends RuntimeException {

  private static final long serialVersionUID = 875925690508143364L;

  private long id = System.currentTimeMillis();
  private String internalMessage;

  public ModelException(String message) {

    this(message, null, null);
  }

  public ModelException(String message, String internalMessage) {

    this(message, internalMessage, null);
  }

  public ModelException(String message, Throwable cause) {

    this(message, null, cause);
  }

  public ModelException(String message, String internalMessage, Throwable cause) {

    super(message == null ? "" : message, cause);
    this.internalMessage = internalMessage == null ? "" : internalMessage;
  }

  public long getId() {

    return id;
  }

  public String getInternalMessage() {

    return internalMessage;
  }

  public String getMessage() {

    return super.getMessage() + " (id = " + id + ")";
  }

  public String getAllMessages() {

    return id + ": " + super.getMessage() + " <" + internalMessage + ">";
  }
}
