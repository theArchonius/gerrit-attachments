/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client;


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
  public String getErrorMessage() {
    return errorMessage;
  }

}
