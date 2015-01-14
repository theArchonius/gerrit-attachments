/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client;

/**
 * Simple implementation of {@link OperationResult}
 * 
 * @author Florian Zoubek
 *
 */
public class SimpleOperationResult implements OperationResult {

  private OperationResultType resultType = OperationResultType.FAILED;

  private String errorMessage = "";

  public SimpleOperationResult(OperationResultType resultType,
      String errorMessage) {
    super();
    this.resultType = resultType;
    this.errorMessage = errorMessage;
  }

  @Override
  public OperationResultType getResultType() {
    return resultType;
  }

  @Override
  public String getErrorMessage() {
    return errorMessage;
  }

  private static final SimpleOperationResult SUCCESS =
      new SimpleOperationResult(OperationResultType.SUCCESS, "");

  /**
   * 
   * @return a result with result type {@link OperationResultType#SUCCESS} and
   *         an empty message
   */
  public static OperationResult getSuccessResult() {
    return SUCCESS;
  }

  /**
   * 
   * @param message the message of the returned result
   * @return a result with result type {@link OperationResultType#FAILED} and
   *         the specified message
   */
  public static OperationResult getErrorResult(String message) {
    return new SimpleOperationResult(OperationResultType.FAILED, message);
  }

  /**
   * 
   * @param message the message of the returned result
   * @return a result with result type {@link OperationResultType#NOTPERMITTED}
   *         and the specified message
   */
  public static OperationResult getNotPermittedResult(String message) {
    return new SimpleOperationResult(OperationResultType.NOTPERMITTED, message);
  }

}
