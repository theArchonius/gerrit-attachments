/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.impl;

import com.eclipsesource.gerrit.plugins.fileattachment.api.OperationResult;
import com.eclipsesource.gerrit.plugins.fileattachment.api.OperationResultType;



/**
 * A simple {@link OperationResult} implementation that allows direct specification of
 * the properties of {@link OperationResult}
 * @author Florian Zoubek
 *
 */
public class GenericOperationResult implements OperationResult{

  /**
   * the result type of the operation
   */
  private OperationResultType resultType;
  
  /**
   * the error message if the result is an error
   */
  private String errorMessage;
  
  public GenericOperationResult(OperationResultType resultType, String errorMessage) {
    this.resultType = resultType;
    this.errorMessage = errorMessage;
  }
  
  @Override
  public OperationResultType getResultType() {
    return resultType;
  }

  @Override
  public String getStatusMessage() {
    return errorMessage;
  }


  private static final GenericOperationResult SUCCESS =
      new GenericOperationResult(OperationResultType.SUCCESS, "");

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
  public static OperationResult createErrorResult(String message) {
    return new GenericOperationResult(OperationResultType.FAILED, message);
  }

  /**
   * 
   * @param message the message of the returned result
   * @return a result with result type {@link OperationResultType#NOTPERMITTED}
   *         and the specified message
   */
  public static OperationResult createNotPermittedResult(String message) {
    return new GenericOperationResult(OperationResultType.NOTPERMITTED, message);
  }
  
}
