/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client;

/**
 * Result types of file attachment operations
 * 
 * @author Florian Zoubek
 *
 */
public enum OperationResultType {
  /**
   * operation was successful
   */
  SUCCESS,
  /**
   * the operation has failed
   */
  FAILED,
  /**
   * the operation was not permitted
   */
  NOTPERMITTED
}
