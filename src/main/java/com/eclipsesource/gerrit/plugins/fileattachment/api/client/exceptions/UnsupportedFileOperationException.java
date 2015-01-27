/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions;

/**
 * This Exceptions indicates that an operation of a FileAttachment service is
 * not supported, by the throwing method or class.
 * 
 * @author Florian Zoubek
 *
 */
public class UnsupportedFileOperationException extends
    FileAttachmentClientException {

  /**
   * 
   */
  private static final long serialVersionUID = 4149438968670741245L;
  
  /**
   * the string id of the operation
   */
  private String operationIdentifier;

  /**
   * @see {@link Exception#Exception()}
   */
  public UnsupportedFileOperationException(String operationIdentifier){
    super();
    this.operationIdentifier = operationIdentifier;
  }

  /**
   * @see {@link Exception#Exception(Throwable)}
   */
  public UnsupportedFileOperationException(String operationIdentifier, Throwable cause){
    super(cause);
  }

  /**
   * @see {@link Exception#Exception(String)}
   */
  public UnsupportedFileOperationException(String operationIdentifier, String message){
    super(message);
  }

  /**
   * @see {@link Exception#Exception(String, Throwable)}
   */
  public UnsupportedFileOperationException(String operationIdentifier, String message, Throwable cause){
    super(message, cause);
  }

  /**
   * @return the string identifier of the operation
   */
  public String getOperationIdentifier() {
    return operationIdentifier;
  }
  
  
}
