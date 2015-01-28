/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client.model;

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
   * the detailed status message containing additional information, warnings and
   * errors, may be empty but not null
   */
  public String getStatusMessage();
}
