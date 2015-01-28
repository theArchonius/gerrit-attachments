/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions;

import com.eclipsesource.gerrit.plugins.fileattachment.api.client.model.OperationResult;

/**
 * @author Florian Zoubek
 *
 */
public class OperationFailedException extends FileAttachmentClientException {

  /**
   * 
   */
  private static final long serialVersionUID = -3957066193340971266L;

  private OperationResult operationResult;

  /**
   * @see {@link Exception#Exception()}
   */
  public OperationFailedException(OperationResult operationResult) {
    super();
    this.operationResult = operationResult;
  }

  /**
   * @see {@link Exception#Exception(Throwable)}
   */
  public OperationFailedException(OperationResult operationResult,
      Throwable cause) {
    super(cause);
    this.operationResult = operationResult;
  }

  /**
   * @see {@link Exception#Exception(String)}
   */
  public OperationFailedException(OperationResult operationResult,
      String message) {
    super(message);
    this.operationResult = operationResult;
  }

  /**
   * @see {@link Exception#Exception(String, Throwable)}
   */
  public OperationFailedException(OperationResult operationResult,
      String message, Throwable cause) {
    super(message, cause);
    this.operationResult = operationResult;
  }

  /**
   * @return the operation result that caused this exception
   */
  public OperationResult getOperationResult() {
    return operationResult;
  }

  @Override
  public String getMessage() {
    StringBuilder combinedMessage = new StringBuilder();
    combinedMessage.append(super.getMessage());
    combinedMessage.append(System.getProperty("line.separator"));
    combinedMessage.append("Operation Result: ");
    combinedMessage.append(operationResult.getResultType().name());
    combinedMessage.append(System.getProperty("line.separator"));
    combinedMessage.append("Status message:");
    combinedMessage.append(System.getProperty("line.separator"));
    combinedMessage.append(operationResult.getStatusMessage());
    return super.getMessage();
  }
}
