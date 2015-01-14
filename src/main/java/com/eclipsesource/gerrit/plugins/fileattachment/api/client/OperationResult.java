/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client;

/**
 * Represents a operation result summary
 * 
 * @author Florian Zoubek
 *
 */
public interface OperationResult {

  /**
   * the result type of the operation
   */
  public OperationResultType getResultType();

  /**
   * the error message or an empty string if no errors occured
   */
  public String getErrorMessage();
}
